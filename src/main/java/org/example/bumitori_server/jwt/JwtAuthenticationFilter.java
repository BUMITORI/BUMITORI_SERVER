package org.example.bumitori_server.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bumitori_server.dto.CustomOAuth2User;
import org.example.bumitori_server.dto.UserProfileDto;
import org.example.bumitori_server.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  public JwtAuthenticationFilter(JWTUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = jwtUtil.resolveToken(request);

    // JWT가 없으면 다음 필터로 넘긴다 (OAuth2 로그인 유도 방지)
    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    if (jwtUtil.validateToken(token)) {
      String role = jwtUtil.getRoleFromToken(token);
      Long userId = jwtUtil.getUserIdFromToken(token);

      // CustomOAuth2User로 생성
      UserProfileDto userProfileDto = new UserProfileDto();
      userProfileDto.setUserId(userId);
      userProfileDto.setRole(Role.valueOf(role));
      CustomOAuth2User customOAuth2User = new CustomOAuth2User(userProfileDto);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
