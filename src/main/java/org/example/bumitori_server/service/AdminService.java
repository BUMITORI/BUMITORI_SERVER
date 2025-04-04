package org.example.bumitori_server.service;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentResponseDto;
import org.example.bumitori_server.dto.CustomOAuth2User;
import org.example.bumitori_server.entity.Absent;
import org.example.bumitori_server.entity.CheckIn;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.enums.EnterStatus;
import org.example.bumitori_server.jwt.JWTUtil;
import org.example.bumitori_server.repository.AbsentRepository;
import org.example.bumitori_server.repository.CheckInRepository;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final AbsentRepository absentRepository;
  private final UserRepository userRepository;
  private final CheckInRepository checkInRepository;

  private final JWTUtil jwtUtil;

  public List<AbsentResponseDto> getAbsentRequests() {
    return absentRepository.findAll().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  public AbsentResponseDto getAbsentDetail(Long absentId) {
    Absent absent = absentRepository.findById(absentId)
        .orElseThrow(() -> new RuntimeException("미입사 신청을 찾을 수 없습니다."));
    return convertToDto(absent);
  }

  public String approveAbsent(Long absentId) {

    // 현재 인증된 사용자 정보 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User)) {
      throw new RuntimeException("인증되지 않은 사용자입니다.");
    }

    // 관리자 userId 조회
    CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
    Long adminUserId = userDetails.getUserId();

    // Admin의 이름 조회
    UserEntity adminUser = userRepository.findByUserId(adminUserId)
        .orElseThrow(() -> new RuntimeException("관리자 정보를 찾을 수 없습니다."));
    String adminName = adminUser.getName();

    // absentId로 신청 정보 조회
    Absent absent = absentRepository.findById(absentId)
        .orElseThrow(() -> new RuntimeException("미입사 신청을 찾을 수 없습니다."));

    // 신청자의 정보 조회
    UserEntity user = userRepository.findByUserId(absent.getUserId())
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    CheckIn checkIn = checkInRepository.findByUserId(absent.getUserId())
        .orElseThrow(() -> new RuntimeException("사용자의 checkIn 정보가 없습니다."));

    // 상태 업데이트
    checkIn.setEnterStatus(EnterStatus.ABSENT);
    checkInRepository.save(checkIn);

    // 관리자의 이름을 absent 테이블에 저장
    absent.setAdminName(adminName);
    absent.setApproval(true);
    absentRepository.save(absent);

    return user.getName() + "님 미입사 신청이 승인되었습니다";
  }

  private AbsentResponseDto convertToDto(Absent absent) {
    UserEntity user = userRepository.findByUserId(absent.getUserId())
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    String roomId = user.getRoomId();
    String roomPrefix = roomId.substring(0, 1);
    String roomNumber = roomId.substring(1);

    return AbsentResponseDto.builder()
        .userId(user.getUserId())
        .absentId(absent.getAbsentId())
        .name(user.getName())
        .roomPrefix(roomPrefix)
        .roomNumber(roomNumber)
        .reason(absent.getReason())
        .specificReason(absent.getSpecificReason())
        .approval(absent.getApproval())
        .absentDate(absent.getAbsentDate())
        .build();
  }
}


