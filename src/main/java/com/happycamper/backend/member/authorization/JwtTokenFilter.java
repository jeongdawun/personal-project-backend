package com.happycamper.backend.member.authorization;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.Role;
import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final String secretKey;
    private final long accessTokenValidTimeMs = 1 * 60 * 60 * 1000; // 1시간 유지 1 * 60 * 60 * 1000
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 쿠키가 없으면 Block
        Cookie[] cookies =  request.getCookies();
        if(cookies == null){
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
            accessToken = JwtUtil.generateToken(email, secretKey, accessTokenValidTimeMs);

            // 쿠키에 새로운 AccessToken 설정 후 응답(토큰과 동일한 유효 시간)
            // 1시간 60 * 60 * 1
            Cookie newAccessTokenCookie = JwtUtil.generateCookie("AccessToken", accessToken, 60 * 60 * 1, false);
            response.addCookie(newAccessTokenCookie);

            log.info("Reissuing the AccessToken token: " + accessToken);
        }

        String email = JwtUtil.getEmail(accessToken, secretKey);

        final Member loginMember = memberService.findMemberByEmail(email);
        final Role role = memberService.findLoginMemberRoleByEmail(email);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginMember.getEmail(), null, List.of(new SimpleGrantedAuthority(role.toString())));

        // Detail 넣어주기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}