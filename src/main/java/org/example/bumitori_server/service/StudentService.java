package org.example.bumitori_server.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentRequestDto;
import org.example.bumitori_server.dto.CustomOAuth2User;
import org.example.bumitori_server.entity.Absent;
import org.example.bumitori_server.entity.CheckIn;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.enums.EnterStatus;
import org.example.bumitori_server.repository.AbsentRepository;
import org.example.bumitori_server.repository.CheckInRepository;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final AbsentRepository absentRepository;
  private final UserRepository userRepository;
  private final CheckInRepository checkInRepository;

  //미입사 신청 요청 처리
  @Transactional
  public void requestAbsent(AbsentRequestDto requestDto){
    Long userId = getAuthenticatedUserId();

    validateUserExists(userId);
    validateAbsentDay(requestDto.getAbsentDate());
    validateDuplicateAbsent(userId, requestDto.getAbsentDate());

    Absent absent = Absent.builder()
        .userId(userId)
        .reason(requestDto.getReason())
        .specificReason(requestDto.getSpecificReason())
        .absentDate(requestDto.getAbsentDate())
        .approval(false)
        .build();

    absentRepository.save(absent);
  }

  // RFID로 학생 입소 처리
  @Transactional
  public Long updateCheckInByRfid(String rfid) {
    UserEntity user = userRepository.findByRfid(rfid)
        .orElseThrow(() -> new RuntimeException("해당 RFID를 가진 사용자를 찾을 수 없습니다: " + rfid));

    CheckIn checkIn = checkInRepository.findByUserId(user.getUserId())
        .orElseThrow(() -> new RuntimeException("ID가 " + user.getUserId() + "인 사용자의 CheckIn 정보가 없습니다."));

    if (checkIn.getEnterStatus() == EnterStatus.ENTERED) {
      throw new IllegalStateException("이미 입사 처리된 사용자입니다");
    }

    checkIn.setEnterTime(LocalDateTime.now());
    checkIn.setEnterStatus(EnterStatus.ENTERED);

    checkInRepository.save(checkIn);
    return user.getUserId();
  }

  // 현재 로그인한 사용자 ID 조회
  private Long getAuthenticatedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User userDetails)) {
      throw new RuntimeException("인증된 사용자 정보가 존재하지 않습니다.");
    }
    return userDetails.getUserId();
  }

  // 사용자 존재 여부 검증
  private void validateUserExists(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new IllegalArgumentException("ID가 " + userId + "인 사용자를 찾을 수 없습니다.");
    }
  }

  // 일요일 여부 검증
  private void validateAbsentDay(LocalDate absentDate) {
    if (absentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
      throw new IllegalArgumentException("미입사 날짜는 일요일만 가능합니다.");
    }
  }

  // 중복 신청 검증
  private void validateDuplicateAbsent(Long userId, LocalDate absentDate) {
    if (absentRepository.existsByUserIdAndAbsentDate(userId, absentDate)) {
      throw new IllegalArgumentException("해당 날짜 미입사 신청이 이미 존재합니다.");
    }
  }
}

