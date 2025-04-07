package org.example.bumitori_server.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.AbsentResponseDto;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

  private final AbsentRepository absentRepository;
  private final UserRepository userRepository;
  private final CheckInRepository checkInRepository;

  public List<AbsentResponseDto> getAbsentRequests() {
    return absentRepository.findAll().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  public AbsentResponseDto getAbsentDetail(Long absentId) {
    Absent absent = findAbsentById(absentId);
    return convertToDto(absent);
  }

  public String approveAbsent(Long absentId) {
    Long adminUserId = getAuthenticatedUserId();
    UserEntity admin = findUserById(adminUserId);

    Absent absent = findAbsentById(absentId);
    UserEntity targetUser = findUserById(absent.getUserId());
    CheckIn checkIn = findCheckInByUserId(absent.getUserId());

    checkIn.setEnterStatus(EnterStatus.ABSENT);
    absent.setApproval(true);
    absent.setAdminName(admin.getName());

    checkInRepository.save(checkIn);
    absentRepository.save(absent);

    return targetUser.getName() + "님 미입사 신청이 승인되었습니다";
  }

  private Absent findAbsentById(Long id) {
    return absentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException
            ("ID가 " + id + "인 미입사 신청이 존재하지 않습니다."));
  }

  private UserEntity findUserById(Long userId) {
    return userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException
            ("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
  }

  private CheckIn findCheckInByUserId(Long userId) {
    return checkInRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException
            ("ID가 " + userId + "인 사용자의 CheckIn 정보가 없습니다."));
  }

  private Long getAuthenticatedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User user)) {
      throw new RuntimeException("인증되지 않은 사용자입니다.");
    }
    return user.getUserId();
  }

  private AbsentResponseDto convertToDto(Absent absent) {
    UserEntity user = findUserById(absent.getUserId());

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



