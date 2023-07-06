package com.happycamper.backend.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService{

//    @Value("${jwt.password}")
    String secretKey = "happycamperhappycamperhappycamperhappycamper";
    String finalSecretKey;
    public String generateAccessToken() {

        finalSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        String token = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS256, finalSecretKey)
                .setExpiration(new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                .compact();
        return token;
    }

    public String generateRefreshToken() {

        String token = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
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
