package org.example.bumitori_server.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bumitori_server.enums.EnterStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long checkInId;

  private Long userId;

  private LocalDateTime enterTime;

  @Enumerated(EnumType.STRING)
  private EnterStatus enterStatus;
}
