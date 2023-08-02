package com.happycamper.backend.config;

import com.happycamper.backend.authorization.JwtTokenFilter;
import com.happycamper.backend.authorization.JwtUtil;
import com.happycamper.backend.domain.member.service.MemberService;
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
    private final MemberService memberService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final String[] permitUrl = { "/", "/member/signup-normal", "/member/signup-business", "/member/login",
                            "/member/check-nickName-duplicate", "/member/check-businessNumber-duplicate", "/member/check-email-duplicate",
                            "/member/check-email-authorize", "/product/check-product-name-duplicate", "/product/list", "/product/topList",
                            "/product/check-vacancy", "/product/check-vacancy-by-date", "/product/category/**", "/product/search/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenFilter(memberService, redisService, jwtUtil, secretKey), UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .authorizeRequests()
                .requestMatchers(permitUrl).permitAll()
                .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/product/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/product/**").authenticated()
                .anyRequest().authenticated()
                .and().build();
    }

}
