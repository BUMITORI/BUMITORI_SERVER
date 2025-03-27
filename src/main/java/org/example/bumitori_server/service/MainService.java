package org.example.bumitori_server.service;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
import org.example.bumitori_server.dto.CheckInResponseDto;
import org.example.bumitori_server.entity.*;
import org.example.bumitori_server.repository.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.bumitori_server.entity.CheckIn.EnterStatus.ENTERED;

@Service
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;
    private final CheckInRepository checkInRepository;
    private final AbsentRepository absentRepository; // 미입사 신청 저장을 위한 Repository

    public List<CheckInResponseDto> getCheckInStatus() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream().map(user -> {
            CheckIn checkIn = checkInRepository.findByUserId(user.getUserId())
                    .orElse(null);

            return new CheckInResponseDto(
                    user.getUserId(),
                    user.getName(),
                    user.getRoomId(),
                    user.getGender(),
                    checkIn != null ? checkIn.getEnterStatus() : null,
                    checkIn != null ? checkIn.getEnterTime() : null
            );
        }).collect(Collectors.toList());
    }

    public Long getUserIdByRfid(String rfid) {
        Optional<UserEntity> user = userRepository.findByRfid(rfid);

        if (user.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        Long userId = user.get().getUserId();
        Optional<CheckIn> checkIn = checkInRepository.findByUserId(userId);

        if (checkIn.isPresent() && checkIn.get().getEnterStatus().equals(ENTERED)) {
            throw new RuntimeException("이미 체크인 되어 있습니다.");
        }

        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setUserId(userId);
        newCheckIn.setEnterStatus(ENTERED);
        newCheckIn.setEnterTime(LocalDateTime.now());

        checkInRepository.save(newCheckIn);

        return userId;
    }

    public void AbsentRequest(AbsentRequestDto requestDto) {
        Optional<UserEntity> user = userRepository.findById(requestDto.getUserId());
        if (user.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        if (requestDto.getAbsentDate().getDayOfWeek() != DayOfWeek.SUNDAY) {
            throw new RuntimeException("미입사 날짜는 일요일만 가능합니다.");
        }

        boolean isAlreadyRequested = absentRepository.existsByUserIdAndAbsentDate(
                requestDto.getUserId(), requestDto.getAbsentDate()
        );
        if (isAlreadyRequested) {
            throw new RuntimeException("이미 해당 날짜에 미입사 신청이 존재합니다.");
        }

        Absent absent = new Absent();
        absent.setUserId(requestDto.getUserId());
        absent.setReason(requestDto.getReason());
        absent.setSpecificReason(requestDto.getSpecificReason());
        absent.setAbsentDate(requestDto.getAbsentDate());
        absentRepository.save(absent);
    }


}

