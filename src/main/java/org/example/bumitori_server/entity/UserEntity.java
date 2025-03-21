package org.example.bumitori_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  private String username;
  private String name;
  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String rfid;

  private Integer studentNo;

  private String roomId;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  public enum Role {
    ADMIN, STUDENT
  }

  public enum Gender {
    M, W
  }
}
