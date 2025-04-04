package org.example.bumitori_server.config;

import org.example.bumitori_server.jwt.JWTUtil;
import org.example.bumitori_server.jwt.JwtAuthenticationFilter;
import org.example.bumitori_server.oauth2.CustomSuccessHandler;
import org.example.bumitori_server.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomSuccessHandler customSuccessHandler;
  private final JWTUtil jwtUtil;

  public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {
    this.customOAuth2UserService = customOAuth2UserService;
    this.customSuccessHandler = customSuccessHandler;
    this.jwtUtil = jwtUtil;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // CSRF 비활성화
    http.csrf(csrf -> csrf.disable());

    // Form 로그인 방식 비활성화
    http.formLogin(form -> form.disable());

    // HTTP Basic 인증 방식 비활성화
    http.httpBasic(httpBasic -> httpBasic.disable());

    // OAuth2 로그인 설정 (필요한 경우만 사용)
    http.oauth2Login(oauth2 ->
        oauth2.userInfoEndpoint(userInfo ->
                userInfo.userService(customOAuth2UserService))
            .successHandler(customSuccessHandler)
            // 기본 로그인 페이지를 사용하지 않도록 loginPage() 설정
            .loginPage("/oauth2/authorization/google")
    );

    // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
    http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

    // 예외 처리: 인증 실패 시 401 Unauthorized 응답 반환 (리다이렉트 방지)
    http.exceptionHandling(exception ->
        exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
    );

    // 경로별 인가 설정
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/checkin", "/absent/request", "/login").permitAll() // 인증 없이 접근 가능
        .requestMatchers("/admin/**").hasAuthority("ADMIN")// JWT가 적용된 상태에서 인증 필요
        .anyRequest().authenticated());

    // 세션 관리: STATELESS (JWT 사용 예정)
    http.sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
