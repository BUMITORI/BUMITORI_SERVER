package org.example.bumitori_server.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.CheckInResponseDto;
import org.example.bumitori_server.entity.Absent;
import org.example.bumitori_server.entity.CheckIn;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.enums.EnterStatus;
import org.example.bumitori_server.repository.AbsentRepository;
import org.example.bumitori_server.repository.CheckInRepository;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.bumitori_server.enums.Role.STUDENT;

@Service
@RequiredArgsConstructor
public class CommonService {

  private final UserRepository userRepository;
  private final CheckInRepository checkInRepository;
  private final AbsentRepository absentRepository;

  @Transactional
  public List<CheckInResponseDto> getCheckInStatus() {
    LocalDate today = LocalDate.now();

    List<UserEntity> students = userRepository.findAll().stream()
        .filter(user -> user.getRole() == STUDENT)
        .sorted(Comparator.comparing(UserEntity::getRoomId))
        .toList();

    List<Long> userIds = students.stream()
        .map(UserEntity::getUserId)
        .toList();

    List<CheckIn> checkIns = checkInRepository.findByUserIdIn(userIds);
    Map<Long, CheckIn> checkInMap = checkIns.stream()
        .collect(Collectors.toMap(CheckIn::getUserId, checkIn -> checkIn));

    updateExpiredAbsents(userIds, checkInMap, today);
    updateOutdatedEntered(checkIns, checkInMap, today);

    return students.stream()
        .map(user -> toCheckInResponseDto(user, checkInMap.get(user.getUserId()), today))
        .toList();
  }

  private void updateExpiredAbsents(List<Long> userIds, Map<Long, CheckIn> checkInMap, LocalDate today) {
    List<Absent> pastAbsents = absentRepository.findByUserIdInAndAbsentDateBefore(userIds, today);

    List<Long> recoverableUserIds = pastAbsents.stream()
        .map(Absent::getUserId)
        .filter(userId -> {
          CheckIn c = checkInMap.get(userId);
          return c != null && c.getEnterStatus() == EnterStatus.ABSENT;
        })
        .distinct()
        .toList();

    if (!recoverableUserIds.isEmpty()) {
      checkInRepository.bulkUpdateEnterStatus(recoverableUserIds, EnterStatus.ABSENT, EnterStatus.NON_ENTER);
      recoverableUserIds.forEach(userId -> {
        CheckIn c = checkInMap.get(userId);
        if (c != null) c.setEnterStatus(EnterStatus.NON_ENTER);
      });
    }
  }

  private void updateOutdatedEntered(List<CheckIn> checkIns, Map<Long, CheckIn> checkInMap, LocalDate today) {
    List<Long> outdatedUserIds = checkIns.stream()
        .filter(c -> c.getEnterStatus() == EnterStatus.ENTERED)
        .filter(c -> c.getEnterTime() == null || !c.getEnterTime().toLocalDate().equals(today))
        .map(CheckIn::getUserId)
        .toList();

    if (!outdatedUserIds.isEmpty()) {
      checkInRepository.bulkUpdateEnterStatus(outdatedUserIds, EnterStatus.ENTERED, EnterStatus.NON_ENTER);
      outdatedUserIds.forEach(userId -> {
        CheckIn c = checkInMap.get(userId);
        if (c != null) c.setEnterStatus(EnterStatus.NON_ENTER);
      });
    }
  }

  private CheckInResponseDto toCheckInResponseDto(UserEntity user, CheckIn checkIn, LocalDate today) {
    String roomId = user.getRoomId();
    String roomPrefix = roomId.substring(0, 1);
    String roomNumber = roomId.substring(1);

    return CheckInResponseDto.builder()
        .name(user.getName())
        .roomPrefix(roomPrefix)
        .roomNumber(roomNumber)
        .gender(user.getGender())
        .enterStatus(checkIn != null ? checkIn.getEnterStatus() : null)
        .enterTime((checkIn != null && checkIn.getEnterTime() != null &&
            checkIn.getEnterTime().toLocalDate().equals(today)) ? checkIn.getEnterTime() : null)
        .build();
  }
}



