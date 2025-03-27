package org.example.bumitori_server.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bumitori_server.entity.Absent;

import java.time.LocalDate;

@Getter
@Setter
public class AbsentRequestDto {
  private Long userId;
  private Absent.Reason reason;
  private String specificReason;
  private LocalDate absentDate;
}
