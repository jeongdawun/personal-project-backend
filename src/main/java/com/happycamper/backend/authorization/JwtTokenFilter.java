package com.happycamper.backend.authorization;

import com.happycamper.backend.domain.authentication.service.CustomUserDetailsService;
import com.happycamper.backend.domain.member.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final RedisService redisService;
    private final CustomUserDetailsService customUserDetailsService;
    private final String secretKey;

    // Jwt 토큰을 검증하는 필터
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 쿠키가 없으면 Block
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.info("There are no cookies");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = null;
        String refreshToken = null;

        // 쿠키가 있으면 AccessToken 가져오기
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("AccessToken")) {
                accessToken = cookie.getValue();
                log.info("AccessToken to validate: " + accessToken);
                break;
            }
        }

        // 가져온 AccessToken이 없으면 Block (만료된 경우 null으로 들어옴)
        if (accessToken == null) {
            log.info("AccessToken is expired");
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("RefreshToken")) {
                    refreshToken = cookie.getValue();
                    log.info("RefreshToken to validate: " + refreshToken);
                    break;
                }
            }

            // RefreshToken이 null이라면 Block
            if (refreshToken == null) {
                log.info("There is no refreshToken");
                return;
            }

            // RefreshToken이 redis에 저장되어 있지 않다면 (로그아웃) Block
            if (redisService.getValueByKey(refreshToken) == null) {
                log.info("The token has been logout");
                return;
            }

            // RefreshToken이 만료되었다면 Block
            if (JwtUtil.isExpired(refreshToken, secretKey)) {
                log.info("RefreshToken is expired");
                return;
            }

            // 위 모든 사항에 걸러지지 않는다면 RefreshToken에서 email을 추출하여
            // 해당 email로 AccessToken 재발행
            String email = JwtUtil.getEmail(refreshToken, secretKey);

            // 1시간 유지 1 * 60 * 60 * 1000
            long accessTokenValidTimeMs = 60 * 60 * 1000;
            accessToken = JwtUtil.generateToken(email, secretKey, accessTokenValidTimeMs);

            // 쿠키에 새로운 AccessToken 설정 후 응답(토큰과 동일한 유효 시간)
            // 1시간 60 * 60 * 1
            Cookie newAccessTokenCookie = JwtUtil.generateCookie("AccessToken", accessToken, 60 * 60, false);
            response.addCookie(newAccessTokenCookie);

            log.info("Reissuing the AccessToken token: " + accessToken);
        }
        String email = JwtUtil.getEmail(accessToken, secretKey);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}