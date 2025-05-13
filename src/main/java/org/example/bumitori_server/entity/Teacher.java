package org.example.bumitori_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

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

  @Min(1) @Max(3)
  private Integer grade;

  @Min(1) @Max(4)
  private Integer classNum;

  private Integer phone;
}
