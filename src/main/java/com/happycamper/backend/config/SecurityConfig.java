package com.happycamper.backend.config;

import com.happycamper.backend.member.authorization.JwtTokenFilter;
import com.happycamper.backend.member.authorization.JwtUtil;
import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                            "/member/check-email-authorize", "/product/list", "/product/**", "/product/check-stock", "/product/category/**",
                            "/product/map-vacancy"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(memberService, redisService, jwtUtil, secretKey), UsernamePasswordAuthenticationFilter.class)
                .cors().and()
                .authorizeRequests()
                .requestMatchers(permitUrl).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
                .anyRequest().authenticated()
                .and().build();
    }

}
