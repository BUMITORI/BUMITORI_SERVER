package org.example.bumitori_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/oauth2/**")
        .allowedOrigins("http://localhost:5173")
        .allowedMethods("GET")
        .allowCredentials(true);
  }
}
