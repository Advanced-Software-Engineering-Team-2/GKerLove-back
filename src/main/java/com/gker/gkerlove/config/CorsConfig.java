package com.gker.gkerlove.config;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active}")
    String env;

    @Override
    public void addCorsMappings(@Nonnull CorsRegistry registry) {
        if (isDevelopment()) {
            registry.addMapping("/**")
                    // 用户前台和管理员后台
                    .allowedOriginPatterns("http://localhost*").allowCredentials(true).allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");
        }
    }

    private boolean isDevelopment() {
        return "dev".equals(env);
    }
}