package org.example.bumitori_server.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.bumitori_server.entity.Absent.Reason;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class AbsentRequestDto {
  private Long userId;
  private String email;
  private Reason reason;
  private String specificReason;
  private LocalDate absentDate;
  private Boolean approved;
  private String name;
}
