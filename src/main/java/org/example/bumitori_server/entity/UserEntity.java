package org.example.bumitori_server.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bumitori_server.enums.Gender;
import org.example.bumitori_server.enums.Role;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  private String email;

  private String name;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String rfid;

  private Integer studentNo;

  private String roomId;

  @Enumerated(EnumType.STRING)
  private Gender gender;
}
