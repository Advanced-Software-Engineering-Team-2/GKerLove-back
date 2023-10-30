package com.gker.gkerlove.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "JWT", paramName = "token", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info().title("果壳之恋后端服务").version("1.0").description("果壳之恋后端服务Api"));
    }

}
