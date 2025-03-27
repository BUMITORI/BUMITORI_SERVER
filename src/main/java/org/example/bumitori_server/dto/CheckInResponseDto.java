package org.example.bumitori_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bumitori_server.entity.CheckIn.EnterStatus;
import org.example.bumitori_server.entity.UserEntity.Gender;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CheckInResponseDto {
    private Long userId;
    private String name;
    private String roomId;
    private Gender gender;
    private EnterStatus enterStatus;
    private LocalDateTime enterTime;
}