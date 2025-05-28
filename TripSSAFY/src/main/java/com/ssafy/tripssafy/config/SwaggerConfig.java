package com.ssafy.tripssafy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		// Swagger에서 JWT를 사용하기 위한 설정.
//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization");

        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Trip.SSAFY API Document")
                        .description("Trip.SSAFY API 명세서입니다.")
                        .version("v1.0.0"));
	}
}
