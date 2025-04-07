package org.example.bumitori_server.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

  private final UserProfileDto userProfileDto;

  public CustomOAuth2User(UserProfileDto userProfileDto) {
    this.userProfileDto = userProfileDto;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Map.of(); // null 대신 빈 Map 반환
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> userProfileDto.getRole().name());
  }

  @Override
  public String getName() {
    return userProfileDto.getName();
  }

  public String getEmail() {
    return userProfileDto.getEmail();
  }

  public Long getUserId() {
    return userProfileDto.getUserId();
  }
}
