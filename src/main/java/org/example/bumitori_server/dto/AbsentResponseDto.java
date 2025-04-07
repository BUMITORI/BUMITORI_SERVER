package org.example.bumitori_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.bumitori_server.enums.Reason;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class AbsentResponseDto {
  private Long userId;
  private Long absentId;
  private String name;
  private String roomPrefix;
  private String roomNumber;
  private Reason reason;
  private String specificReason;
  private Boolean approval;
  private LocalDate absentDate;
}
