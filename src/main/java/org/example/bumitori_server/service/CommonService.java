package org.example.bumitori_server.service;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.CheckInResponseDto;
import org.example.bumitori_server.entity.CheckIn;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.repository.CheckInRepository;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.bumitori_server.entity.CheckIn.EnterStatus.ENTERED;
import static org.example.bumitori_server.entity.CheckIn.EnterStatus.NON_ENTER;
import static org.example.bumitori_server.entity.UserEntity.Role.STUDENT;

@Service
@RequiredArgsConstructor
public class CommonService {
  private final UserRepository userRepository;
  private final CheckInRepository checkInRepository;

  public List<CheckInResponseDto> getCheckInStatus() {
    LocalDate today = LocalDate.now();

    // 모든 학생 조회
    List<UserEntity> students = userRepository.findAll();
    List<Long> userIds = students.stream().map(UserEntity::getUserId).collect(Collectors.toList());

    // 모든 CheckIn 데이터 한 번에 조회 (쿼리 1번)
    List<CheckIn> checkIns = checkInRepository.findByUserIdIn(userIds);

    // Map으로 변환 (userId -> CheckIn 매핑)
    Map<Long, CheckIn> checkInMap = checkIns.stream()
        .collect(Collectors.toMap(CheckIn::getUserId, checkIn -> checkIn));

    return students.stream()
        .filter(user -> user.getRole() == STUDENT)
        .sorted(Comparator.comparing(UserEntity::getRoomId))
        .map(user -> {
          CheckIn checkIn = checkInMap.get(user.getUserId());

          if (checkIn != null && checkIn.getEnterStatus() == ENTERED
              && !checkIn.getEnterTime().toLocalDate().equals(today)) {
            checkIn.setEnterStatus(NON_ENTER);
            checkInRepository.save(checkIn);
          }

          String roomId = user.getRoomId();
          String roomPrefix = roomId.substring(0, 1);
          String roomNumber = roomId.substring(1);

          return new CheckInResponseDto(
              user.getName(),
              roomPrefix,
              roomNumber,
              user.getGender(),
              checkIn != null ? checkIn.getEnterStatus() : null,
              (checkIn != null && checkIn.getEnterTime() != null && checkIn.getEnterTime().toLocalDate().equals(today))
                  ? checkIn.getEnterTime()
                  : null
          );
        })
        .collect(Collectors.toList());
  }
}