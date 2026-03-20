package com.xueya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityEnhancementConfig {

    // CORS配置
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*").allowCredentials(true);
            }
        };
    }

    // 可以在这里添加更多安全相关的配置，如：
    // 1. IP限制过滤器
    // 2. 验证码生成和验证服务
    // 3. 防止暴力破解的安全措施
    // 4. 其他安全相关的配置
}
