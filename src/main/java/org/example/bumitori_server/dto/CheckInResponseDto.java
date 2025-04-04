package org.example.bumitori_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.bumitori_server.enums.EnterStatus;
import org.example.bumitori_server.enums.Gender;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CheckInResponseDto {
  private String name;
  private String roomPrefix;
  private String roomNumber;
  private Gender gender;
  private EnterStatus enterStatus;
  private LocalDateTime enterTime;
}