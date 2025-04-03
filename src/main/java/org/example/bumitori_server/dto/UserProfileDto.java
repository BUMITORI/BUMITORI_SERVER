package org.example.bumitori_server.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bumitori_server.entity.Role;


@Getter
@Setter
public class UserProfileDto {
  private Role role;
  private Long userId;
  private String name;
  private String username;
  private String email;
}
