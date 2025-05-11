package org.example.bumitori_server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.example.bumitori_server.enums.Role;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long teacherId;

  private Long userId;

  private String name;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Min(1) @Max(3)
  private Integer grade;

  @Min(1) @Max(4)
  private Integer classNum;

  private Integer phone;
}
