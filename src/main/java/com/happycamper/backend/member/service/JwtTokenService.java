package com.happycamper.backend.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("${jwt.password}")
    String secretKey;

    String finalSecretKey;

    public String generateAccessToken(String email) {

        finalSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        String token = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS256, finalSecretKey)
                .setExpiration(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000))
                .compact();
        return token;
    }

    public String generateRefreshToken(String email) {

        String token = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS256, finalSecretKey)
                .setExpiration(new Date(System.currentTimeMillis() + 366 * 60 * 60 * 1000))
                .compact();
        return token;
    }

    public Claims parseJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(finalSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
