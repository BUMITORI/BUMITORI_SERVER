package org.example.bumitori_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Absent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long absentId;

  private Long userId;

  @Enumerated(EnumType.STRING)
  private Reason reason;

  private String specificReason;

  @Column(columnDefinition = "BOOLEAN default false")
  @Builder.Default
  private Boolean approval = false;

  private String adminName;

  private LocalDate absentDate;

  public enum Reason {
    SICK_LEAVE, INTERNATIONAL_ACTIVITY, FAMILY_PROMISE, OTHERS
  }
}
