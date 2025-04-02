package org.example.bumitori_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CheckIn {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long checkInId;

  private Long userId;

  private LocalDateTime enterTime;

  @Enumerated(EnumType.STRING)
  private EnterStatus enterStatus;

  public enum EnterStatus {
    ENTERED, NON_ENTER, ABSENT
  }
}
