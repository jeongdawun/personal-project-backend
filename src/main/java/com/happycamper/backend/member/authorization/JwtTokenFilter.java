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
    private final long accessTokenValidTimeMs = 60 * 60 * 1000; // 1시간 유지
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰이 없으면 Block
        Cookie[] cookies =  request.getCookies();
        if(cookies == null){
            log.error("쿠키가 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키안에 accessToken 유효성 검사
        if (cookies != null) {
            for (Cookie accessCookie : cookies) {
                if (accessCookie.getName().equals("AccessToken")) {

                    String accessToken = accessCookie.getValue();

                    // 쿠키 안에 AccessToken 값이 없다면 Block
                    // 로그아웃을 했거나, 만료되었거나
                    if(accessToken == null) {
                        log.error("accessToken이 없습니다.");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    // AccessToken이 있지만 만료된 경우
                    if(jwtUtil.isExpired(accessToken, secretKey)) {
                        for (Cookie refreshCookie : cookies) {
                            if (refreshCookie.getName().equals("RefreshToken")) {

                                // 쿠키에서 refreshToken 추출
                                String refreshToken = refreshCookie.getValue();

                                // refreshToken이 없다면 Block
                                // 로그아웃을 했거나, 만료되었거나
                                if(refreshToken == null) {
                                    log.error("refreshToken이 없습니다.");
                                    filterChain.doFilter(request, response);
                                    return;
                                }

                                // 쿠키에는 refreshToken이 있지만 redis에서 value를 조회할 수 없다면 Block
                                // 로그아웃하여 redis에서 refresToken을 제거했을 경우
                                if(redisService.getValueByKey(refreshToken) == null) {
                                    log.error("로그아웃된 토큰입니다.");
                                    filterChain.doFilter(request, response);
                                    return;
                                }

                                // 쿠키에 refreshToken이 있고, 만료되지 않은 경우
                                if(!jwtUtil.isExpired(refreshToken, secretKey)) {

                                    // refreshToken으로 accessToken 재발행
                                    String email = jwtUtil.getEmail(refreshToken, secretKey);
                                    accessToken = jwtUtil.generateToken(email, secretKey, accessTokenValidTimeMs);
                                    accessCookie = jwtUtil.generateCookie("AccessToken", accessToken, 60 * 60, false);

                                    // 쿠키에 설정 후 응답
                                    response.addCookie(accessCookie);
                                    break;
                                } else {
                                    log.error("refreshToken이 만료되었습니다.");
                                    filterChain.doFilter(request, response);
                                    return;
                                }
                            }
                        }
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
        }

    }
}
