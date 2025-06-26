package org.example.bumitori_server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    String jwtSchemeName = "JWT";

    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
    SecurityScheme securityScheme = new SecurityScheme()
        .name(jwtSchemeName)
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");

    Components components = new Components()
        .addSecuritySchemes(jwtSchemeName, securityScheme);

    Server server = new Server();
    server.setUrl("http://localhost:8080");
//    server.setUrl("https://back-bumitori.jamkris.kro.kr");

    return new OpenAPI()
        .info(new Info()
            .title("BUMITORI API")
            .version("v1")
            .description("Bumitori API 명세서")
        )
        .addSecurityItem(securityRequirement)
        .components(components)
        .addServersItem(server);
  }
}
