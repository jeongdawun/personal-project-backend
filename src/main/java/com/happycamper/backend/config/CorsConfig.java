package com.happycamper.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${ec2.server1}")
    private String ec2url;

    @Value("${ec2.server2}")
    private String ec2url2;

    @Value("${ec2.server3}")
    private String ec2url3;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://127.0.0.1:8080",
                        "http://localhost:8080",
                        ec2url, ec2url2, ec2url3)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders(HttpHeaders.AUTHORIZATION)
                .allowCredentials(true);
    }
}
