package org.example.bumitori_server.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bumitori_server.dto.CustomOAuth2User;
import org.example.bumitori_server.entity.UserEntity;
import org.example.bumitori_server.jwt.JWTUtil;
import org.example.bumitori_server.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JWTUtil jwtUtil;
  private final UserRepository userRepository;

  public CustomSuccessHandler(
      JWTUtil jwtUtil,
      UserRepository userRepository
  ) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) throws IOException, ServletException {
    CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
    String email = customUser.getEmail();

    Optional<UserEntity> userOpt = userRepository.findByEmail(email);
    UserEntity user = userOpt.orElseThrow();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> it = authorities.iterator();
    String role = String.valueOf(user.getRole());
    Long userId = user.getUserId();

    String token = jwtUtil.createJwt(userId, role, 60 * 60 * 60L);

    response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    response.addCookie(createCookie("Authorization", token));
    response.sendRedirect("http://localhost:5173/");
  }

  private Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(60 * 60 * 60);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    return cookie;
  }
}
