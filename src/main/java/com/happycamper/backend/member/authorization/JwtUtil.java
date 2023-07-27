package com.happycamper.backend.member.authorization;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // 토큰 만료 여부 검증(현재 시간 기준으로 만료되었는지 확인)
    public static boolean isExpired(String token, String secretKey) {

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Date expirationTime = claimsJws.getBody().getExpiration();
            Date now = new Date();

            return expirationTime != null && expirationTime.before(now);

        } catch (ExpiredJwtException e) {
            log.info("Token is expired");
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    // Jwt 토큰 생성
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
        } catch (ExpiredJwtException e) {
            log.info("Token is expired");
            return null;
        } catch (SignatureException e) {
            log.info("Signature of token is not correct");
            return null;
        }
    }

    // Cookie 생성
    public static Cookie generateCookie(String tokenName, String token, int validTimeS, Boolean httponly) {

        Cookie cookie = new Cookie(tokenName, token);
        cookie.setPath("/");
        cookie.setHttpOnly(httponly);
        cookie.setMaxAge(validTimeS);

        return cookie;
    }

    // Jwt 토큰에서 Claims 추출
    public static Claims extractClaims(String token, String secretKey) {

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Jwt 토큰에서 Subject 추출
    public static String getEmail(String token, String secretKey) {

        return extractClaims(token, secretKey).getSubject();
    }

    // Cookie에서 Token 추출
    public static String extractTokenByCookie(HttpServletRequest request, String CookieName) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CookieName)) {

                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}