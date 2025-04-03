package org.example.bumitori_server.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

  private final UserProfileDto userProfileDto;

  public CustomOAuth2User(UserProfileDto userProfileDto) {
    this.userProfileDto = userProfileDto;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> collection = new ArrayList<>();

    collection.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return userProfileDto.getRole().name();
      }
    });
    return collection;
  }

  @Override
  public String getName() {
    return userProfileDto.getName();
  }

  public String getEmail() {return userProfileDto.getEmail(); }

  public String getUsername() {
    return userProfileDto.getUsername();
  }
}