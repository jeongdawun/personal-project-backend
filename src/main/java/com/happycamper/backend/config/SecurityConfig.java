package com.happycamper.backend.config;

import com.happycamper.backend.domain.authentication.service.CustomUserDetailsService;
import com.happycamper.backend.authorization.JwtTokenFilter;
import com.happycamper.backend.domain.member.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.password}")
    private String secretKey;
    private final RedisService redisService;
    private final CustomUserDetailsService customUserDetailsService;
    private final String[] permitUrl = { "/", "/member/signup-normal", "/member/signup-business", "/member/login",
                            "/member/check-nickName-duplicate", "/member/check-businessNumber-duplicate", "/member/check-email-duplicate",
                            "/member/check-email-authorize", "/product/check-product-name-duplicate", "/product/list", "/product/topList",
                            "/product/check-vacancy", "/product/check-vacancy-by-date", "/product/category/**", "/product/search/**",
                            "/member/check-role"};
    private final String[] permitNormalUrl = {"/member/auth-userProfile", "/member/profile-register"};
    private final String[] permitBusinessUrl = {"/member/auth-sellerInfo", "/member/sellerInfo-register", "/product/register",
                                                "/product/myList", "/product/myList"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .addFilterBefore(new JwtTokenFilter(redisService, customUserDetailsService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(permitNormalUrl).hasAuthority("NORMAL")
                        .requestMatchers(permitBusinessUrl).hasAuthority("BUSINESS")
                        .requestMatchers(HttpMethod.DELETE, "/product/**").hasAuthority("BUSINESS")
                        .requestMatchers(HttpMethod.PUT, "/product/**").hasAuthority("BUSINESS")
                        .requestMatchers(permitUrl).permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
