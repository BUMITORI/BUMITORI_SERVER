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

  public CustomSuccessHandler(JWTUtil jwtUtil, UserRepository userRepository) {

    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    //OAuth2User
    CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

    String email = customUserDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    String role = String.valueOf(user.get().getRole());
    Long userId = user.get().getUserId();

    String token = jwtUtil.createJwt(userId, role, 60 * 60 * 60L);

    response.addCookie(createCookie("Authorization", token));
    response.sendRedirect("http://localhost:5173/");
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(60 * 60 * 60);
    //cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setHttpOnly(true);

    return cookie;
  }
}