package org.example.bumitori_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Absent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "studentNo", referencedColumnName = "studentNo", nullable = false)
  private UserEntity user;

  @Enumerated(EnumType.STRING)
  private Reason reason;

  private String specificReason;

  @Column(columnDefinition = "BOOLEAN default false")
  private Boolean approval = false;

  private String adminName;

  public enum Reason {
    SICK_LEAVE, INTERNATIONAL_ACTIVITY, FAMILY_PROMISE, OTHERS
  }
}
