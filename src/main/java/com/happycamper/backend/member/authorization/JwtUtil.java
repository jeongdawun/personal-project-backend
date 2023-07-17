package com.happycamper.backend.member.authorization;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    // 토큰 만료 여부 검증(현재 시간 기준으로 만료되었는지 확인)
    public static boolean isExpired(String token, String secretKey) {
        try {
            Date expirationTime = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody().getExpiration();

            return expirationTime.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    // 토큰 생성(accessToken은 만료 시간을 짧게, refreshToken은 길게 설정한다.)
    // accessToken 6시간, refreshToken 2주
    public static String generateToken(String email, String secretKey, long validTimeMs) {

        String token = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + validTimeMs))
                .compact();
        return token;
    }

    // secretKey로 토큰 검증
    public static Claims parseJwtToken(String token, String secretKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("exception!!!!!!!");
            return null;
        }
    }

    // 쿠키 생성
    public static Cookie generateCookie(String tokenName, String token, int validTimeS, Boolean httponly) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setPath("/");
        cookie.setHttpOnly(httponly);
        cookie.setMaxAge(validTimeS);

        return cookie;
    }

    // 토큰에서 Claims 추출
    public static Claims extractClaims(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 email 추출
    public static String getEmail(String token, String secretKey) {
        return extractClaims(token, secretKey).getSubject();
    }
}