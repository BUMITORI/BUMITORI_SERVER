package org.example.bumitori_server.service;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
import org.example.bumitori_server.entity.Absent;
import org.example.bumitori_server.entity.CheckIn;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.repository.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.bumitori_server.entity.CheckIn.EnterStatus.ENTERED;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserRepository userRepository;
    private final CheckInRepository checkInRepository;
    private final AbsentRepository absentRepository;

    public String getEmailByRfid(String rfid) {
        Optional<UserEntity> user = userRepository.findByRfid(rfid);

        if (user.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        String eamil = user.get().getEmail();
        Optional<CheckIn> checkIn = checkInRepository.findByEmail(eamil);

        if (checkIn.isPresent() && checkIn.get().getEnterStatus().equals(ENTERED)) {
            throw new RuntimeException("이미 체크인 되어 있습니다.");
        }

        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setEmail(eamil);
        newCheckIn.setEnterStatus(ENTERED);
        newCheckIn.setEnterTime(LocalDateTime.now());

        checkInRepository.save(newCheckIn);

        return eamil;
    }

    public void AbsentRequest(AbsentRequestDto requestDto) {
        Optional<UserEntity> user = userRepository.findByEmail(requestDto.getEmail());
        if (user.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        if (requestDto.getAbsentDate().getDayOfWeek() != DayOfWeek.SUNDAY) {
            throw new RuntimeException("미입사 날짜는 일요일만 가능합니다.");
        }

        boolean isAlreadyRequested = absentRepository.existsByEmailAndAbsentDate(
            requestDto.getEmail(), requestDto.getAbsentDate()
        );
        if (isAlreadyRequested) {
            throw new RuntimeException("이미 해당 날짜에 미입사 신청이 존재합니다.");
        }

        Absent absent = new Absent();
        absent.setEmail(requestDto.getEmail());
        absent.setReason(requestDto.getReason());
        absent.setSpecificReason(requestDto.getSpecificReason());
        absent.setAbsentDate(requestDto.getAbsentDate());
        absentRepository.save(absent);
    }
}
