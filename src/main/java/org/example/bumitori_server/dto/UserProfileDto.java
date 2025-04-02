package org.example.bumitori_server.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.bumitori_server.entity.UserEntity;

@Getter
@Setter
public class UserProfileDto {
  private UserEntity.Role role;
  private String name;
  private String username;
  private String email;
}
