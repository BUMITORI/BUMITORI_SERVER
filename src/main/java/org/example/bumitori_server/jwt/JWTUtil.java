package org.example.bumitori_server.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
  private final SecretKey secretKey;

  public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
    this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = parseToken(token);
    Object userIdObj = claims.get("userId");
    if (userIdObj instanceof Number number) return number.longValue();
    if (userIdObj instanceof String str) return Long.parseLong(str);
    throw new IllegalArgumentException("토큰에 유효한 userId가 포함되어 있지 않습니다.");
  }

  public String getRoleFromToken(String token) {
    return parseToken(token).get("role", String.class);
  }

  public boolean validateToken(String token) {
    try {
      return !parseToken(token).getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Claims parseToken(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String createJwt(Long userId, String role, Long expiredMs) {
    return Jwts.builder()
        .claim("userId", userId)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact();
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("Authorization".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
