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

  @OneToOne
  @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false, unique = true)
  private UserEntity user;

  private LocalDateTime enterTime;

  @Enumerated(EnumType.STRING)
  private EnterStatus enterStatus;

  public enum EnterStatus {
    ENTERED, NON_ENTERED, ABSENT
  }
}
