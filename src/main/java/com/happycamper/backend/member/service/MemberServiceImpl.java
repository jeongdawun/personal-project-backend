package com.happycamper.backend.member.service;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.MemberRole;
import com.happycamper.backend.member.entity.Role;
import com.happycamper.backend.member.repository.MemberRepository;
import com.happycamper.backend.member.repository.MemberRoleRepository;
import com.happycamper.backend.member.repository.RoleRepository;
import com.happycamper.backend.member.service.request.NormalMemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    final private MemberRepository memberRepository;
    final private MemberRoleRepository memberRoleRepository;
    final private RoleRepository roleRepository;

    @Override
    public Boolean normalMemberRegister(NormalMemberRegisterRequest request) {
        // 계정 생성
        final Member member = memberRepository.save(request.toMember());

        // 회원 타입 부여
        final Role role = roleRepository.findByRoleType(request.getRoleType()).get();
        final MemberRole memberRole = new MemberRole(role, member);
        memberRoleRepository.save(memberRole);

        return true;
    }

}
