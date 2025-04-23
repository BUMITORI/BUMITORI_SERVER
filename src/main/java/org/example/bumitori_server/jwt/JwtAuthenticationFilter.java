package org.example.bumitori_server.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.bumitori_server.dto.CustomOAuth2User;
import org.example.bumitori_server.dto.UserProfileDto;
import org.example.bumitori_server.enums.Role;
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

    // Swagger 경로에 대해 필터 제외
    String uri = request.getRequestURI();
    if (uri.startsWith("/v3/api-docs") ||
        uri.startsWith("/swagger-ui") ||
        uri.startsWith("/swagger-resources") ||
        uri.startsWith("/webjars")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = jwtUtil.resolveToken(request);

    if (token != null && jwtUtil.validateToken(token)) {
      Long userId = jwtUtil.getUserIdFromToken(token);
      String roleStr = jwtUtil.getRoleFromToken(token);

      try {
        Role role = Role.valueOf(roleStr.toUpperCase());

        UserProfileDto userProfileDto = UserProfileDto.builder()
            .userId(userId)
            .role(role)
            .build();

        CustomOAuth2User principal = new CustomOAuth2User(userProfileDto);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (IllegalArgumentException e) {
        // 유효하지 않은 Role이면 인증 안 함
      }
    }

    filterChain.doFilter(request, response);
  }
}
