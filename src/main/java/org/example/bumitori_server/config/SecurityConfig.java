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
        http.csrf(csrf -> csrf.disable());

        // Form 로그인 방식 비활성화
        http.formLogin(form -> form.disable());

        // HTTP Basic 인증 방식 비활성화
        http.httpBasic(httpBasic -> httpBasic.disable());

        // OAuth2 로그인 설정 (테스트를 위해 비활성화)
        http.oauth2Login(oauth2 ->
                oauth2.userInfoEndpoint(userInfo ->
                        userInfo.userService(customOAuth2UserService)).successHandler(customSuccessHandler));


        // JWT 인증 부분 주석 처리 (임시 비활성화)
        //http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


        // 경로별 인가 작업
        http.authorizeHttpRequests(auth ->
            auth.requestMatchers("/", "/checkin", "/absent/request").permitAll() // 인증 없이 접근 가능
                .requestMatchers("/admin/**").permitAll() // 관리자 페이지 접근 가능 (테스트용)
                .anyRequest().authenticated());

        // 세션 관리: STATELESS (JWT 사용 예정)
        http.sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}