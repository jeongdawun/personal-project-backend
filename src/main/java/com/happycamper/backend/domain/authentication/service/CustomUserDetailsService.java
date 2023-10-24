package com.happycamper.backend.domain.authentication.service;

import com.happycamper.backend.domain.authentication.entity.CustomUserDetail;
import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.member.entity.MemberRole;
import com.happycamper.backend.domain.member.repository.MemberRoleRepository;
import com.happycamper.backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberService memberService;
    private final MemberRoleRepository memberRoleRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Member member = memberService.findMemberByEmail(email);
        Optional<MemberRole> memberRole = memberRoleRepository.findByMember(member);

        UserDetails userDetails = CustomUserDetail.builder()
                .username(email)
                .password(member.getPassword())
                .userNo(member.getId())
                .authorities(Collections.singleton(new SimpleGrantedAuthority(memberRole.get().getRole().getRoleType().toString())))
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .build();

        return userDetails;
    }
}
