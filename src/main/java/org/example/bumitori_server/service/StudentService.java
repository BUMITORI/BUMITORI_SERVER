package org.example.bumitori_server.service;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
import org.example.bumitori_server.dto.CustomOAuth2User;
import org.example.bumitori_server.entity.Absent;
import org.example.bumitori_server.entity.CheckIn;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.repository.AbsentRepository;
import org.example.bumitori_server.repository.CheckInRepository;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
  private final UserRepository userRepository;
  private final CheckInRepository checkInRepository;
  private final AbsentRepository absentRepository;

  public Long updateCheckInByRfid(String rfid) {
    Optional<UserEntity> user = userRepository.findByRfid(rfid);

    if (user.isEmpty()) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }

    Long userId = user.get().getUserId();
    Optional<CheckIn> checkIn = checkInRepository.findByUserId(userId);

    if (checkIn.isPresent()) {
      CheckIn existingCheckIn = checkIn.get();

      if (existingCheckIn.getEnterStatus().equals(CheckIn.EnterStatus.ENTERED)) {
        throw new RuntimeException("이미 체크인 되어 있습니다.");
      }

      existingCheckIn.setEnterStatus(CheckIn.EnterStatus.ENTERED);
      existingCheckIn.setEnterTime(LocalDateTime.now());

      checkInRepository.save(existingCheckIn);
    } else {
      CheckIn newCheckIn = new CheckIn();
      newCheckIn.setUserId(userId);
      newCheckIn.setEnterStatus(CheckIn.EnterStatus.ENTERED);
      newCheckIn.setEnterTime(LocalDateTime.now());

      checkInRepository.save(newCheckIn);
    }

    return userId;
  }


  public void AbsentRequest(AbsentRequestDto requestDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User)) {
      throw new RuntimeException("인증되지 않은 사용자입니다.");
    }

    CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    Optional<UserEntity> user = userRepository.findByUserId(userId);
    if (user.isEmpty()) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }

    if (requestDto.getAbsentDate().getDayOfWeek() != DayOfWeek.SUNDAY) {
      throw new RuntimeException("미입사 날짜는 일요일만 가능합니다.");
    }

    boolean isAlreadyRequested = absentRepository.existsByUserIdAndAbsentDate(
        userId, requestDto.getAbsentDate()
    );
    if (isAlreadyRequested) {
      throw new RuntimeException("이미 해당 날짜에 미입사 신청이 존재합니다.");
    }

    Absent absent = new Absent();
    absent.setUserId(userId);
    absent.setReason(requestDto.getReason());
    absent.setSpecificReason(requestDto.getSpecificReason());
    absent.setAbsentDate(requestDto.getAbsentDate());

    absentRepository.save(absent);
  }

}
