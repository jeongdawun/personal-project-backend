package com.happycamper.backend.member.service;

import com.happycamper.backend.exception.Exceptions;
import com.happycamper.backend.member.entity.Role;
import io.jsonwebtoken.*;
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
        try {
            return Jwts.parser()
                    .setSigningKey(finalSecretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("exception!!!!!!!");
            return null;
        }

//        catch (ExpiredJwtException e) {
//            throw new Exceptions("만료 토큰", e);
//        } catch (MalformedJwtException e) {
//            throw new Exceptions("형식 확인 필요", e);
//        } catch (SignatureException e) {
//            throw new Exceptions("서명 확인 필요", e);
//        }
    }
}
