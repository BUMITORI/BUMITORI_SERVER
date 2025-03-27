package org.example.bumitori_server.config;

import org.example.bumitori_server.jwt.JWTUtil;
import org.example.bumitori_server.oauth2.CustomSuccessHandler;
import org.example.bumitori_server.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
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
        http.csrf(auth -> auth.disable());

        // Form 로그인 방식 비활성화
        http.formLogin(auth -> auth.disable());

        // HTTP Basic 인증 방식 비활성화
        http.httpBasic(auth -> auth.disable());

        /* OAuth2 로그인 설정 (테스트를 위해 비활성화)
        http.oauth2Login(oauth2 ->
                oauth2.userInfoEndpoint(userInfo ->
                        userInfo.userService(customOAuth2UserService)).successHandler(customSuccessHandler));
        */

        // 경로별 인가 작업

        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("/", "/checkin", "/absent/request").permitAll() // checkin 엔드포인트 허용
                        .anyRequest().authenticated());


        // 세션 관리: STATELESS (JWT 사용)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
