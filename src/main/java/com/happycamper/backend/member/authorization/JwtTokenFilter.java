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
    private final long accessTokenValidTimeMs = 6 * 60 * 60 * 1000; // 6시간 유지
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키가 없으면 Block
        Cookie[] cookies =  request.getCookies();
        if(cookies == null){
            log.info("쿠키가 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = null;
        String refreshToken = null;

        // 쿠키가 있으면 accessToken 가져오기
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("AccessToken")) {
                accessToken = cookie.getValue();
                log.info("검증할 accessToken: " + accessToken);
                break;
            }
        }

        // 가져온 accessToken이 없으면 Block
        if (accessToken == null) {
            log.info("accessToken이 없습니다.");
            return;
        }

        // 가져온 accessToken의 만료 시간이 10분이 남아있지 않으면
        // 쿠키에서 refreshToken 꺼내기
        if (jwtUtil.tenMinutesBeforeExpired(accessToken, secretKey)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("RefreshToken")) {
                    refreshToken = cookie.getValue();
                    log.info("검증할 refreshToken: " + refreshToken);
                    break;
                }
            }
            // refreshToken이 null이라면 Block
            if (refreshToken == null) {
                log.info("refreshToken이 없습니다.");
                return;
            }

            // refreshToken이 redis에 저장되어 있지 않다면 (로그아웃) Block
            if (redisService.getValueByKey(refreshToken) == null) {
                log.info("로그아웃된 토큰입니다.");
                return;
            }

            // refreshToken이 만료되었다면 Block
            if (jwtUtil.isExpired(refreshToken, secretKey)) {
                log.info("refreshToken이 만료되었습니다.");
                return;
            }

            // 위 모든 사항에 걸러지지 않는다면 refreshToken에서 email을 추출하여
            // 해당 email로 accessToken 재발행
            String email = jwtUtil.getEmail(refreshToken, secretKey);
            accessToken = jwtUtil.generateToken(email, secretKey, accessTokenValidTimeMs);

            // 쿠키에 새로운 accessToken 설정 후 응답(토큰과 동일한 유효 시간)
            // 6시간 60 * 60 * 6
            Cookie newAccessTokenCookie = jwtUtil.generateCookie("AccessToken", accessToken, 60 * 60 * 6, false);
            response.addCookie(newAccessTokenCookie);

            log.info("accessToken 토큰 재발행: " + accessToken);
        }

        String email = jwtUtil.getEmail(accessToken, secretKey);

        Member loginMember = memberService.findLoginMemberByEmail(email);
        Role role = memberService.findLoginMemberRoleByEmail(email);

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