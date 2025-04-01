package org.example.bumitori_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bumitori_server.entity.Absent.Reason;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AbsentResponseDto {
  private Long userId;
  private Long absentId;
  private String name;
  private String roomId;
  private Reason reason;
  private String specificReason;
  private Boolean approval;
  private LocalDate absentDate;
}
