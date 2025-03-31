package org.example.bumitori_server.service;

import lombok.RequiredArgsConstructor;
import org.example.bumitori_server.dto.CheckInResponseDto;
import org.example.bumitori_server.entity.CheckIn;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.repository.AbsentRepository;
import org.example.bumitori_server.repository.CheckInRepository;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
        List<UserEntity> users = userRepository.findAll();
        LocalDate today = LocalDate.now();

        return users.stream()
            .filter(user -> STUDENT.equals(user.getRole()))
            .map(user -> {
                CheckIn checkIn = checkInRepository.findByEmail(user.getEmail()).orElse(null);

//                if (checkIn != null && checkIn.getEnterStatus() == ENTERED && !checkIn.getEnterTime().toLocalDate().equals(today)) {
//                    checkIn.setEnterStatus(NON_ENTER);
//                    checkInRepository.save(checkIn);
//                }

                return new CheckInResponseDto(
                    user.getEmail(),
                    user.getName(),
                    user.getRoomId(),
                    user.getGender(),
                    checkIn != null ? checkIn.getEnterStatus() : null,
                    checkIn != null ? checkIn.getEnterTime() : null
                );
            })
            .collect(Collectors.toList());
    }
}
