package org.example.bumitori_server.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
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
    Object userIdObj = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userId");
    if (userIdObj instanceof Number) {
      return ((Number) userIdObj).longValue();
    } else if (userIdObj instanceof String) {
      return Long.parseLong((String) userIdObj);
    } else {
      throw new IllegalArgumentException("Invalid userId type in token");
    }  }

  public String getRoleFromToken(String token) {
    return Jwts.parser().verifyWith(secretKey)
        .build().parseSignedClaims(token).getPayload()
        .get("role", String.class);
  }

  public boolean validateToken(String token) {
    try {
      return !Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      return false;
    }
  }

  public String createJwt(Long userId, String role, Long expiredMs) {
    return Jwts.builder()
        .claim("userId", userId)
        .claim("role", role)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact();
  }

  public String resolveToken(HttpServletRequest request) {
    // 우선 헤더에서 토큰 추출
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    // 헤더에 없으면 쿠키에서 토큰 추출
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("Authorization".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    System.out.println("sadfdas");
    return null;
  }
}
