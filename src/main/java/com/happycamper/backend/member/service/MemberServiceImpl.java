package com.happycamper.backend.member.service;

import com.happycamper.backend.member.controller.form.CheckEmailDuplicateRequestForm;
import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.MemberRole;
import com.happycamper.backend.member.entity.Role;
import com.happycamper.backend.member.repository.MemberRepository;
import com.happycamper.backend.member.repository.MemberRoleRepository;
import com.happycamper.backend.member.repository.RoleRepository;
import com.happycamper.backend.member.service.request.BusinessMemberRegisterRequest;
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

    // 일반 회원의 회원가입
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

    // 이메일 중복 확인
    @Override
    public Boolean checkEmailDuplicate(CheckEmailDuplicateRequestForm requestForm) {
        // 존재하는 계정인지 확인
        Optional<Member> maybeMember = memberRepository.findByEmail(requestForm.getEmail());

        if(maybeMember.isPresent()) {
            return true;
        }
        return false;
    }

    // 사업자 회원의 회원가입
    @Override
    public Boolean businessMemberRegister(BusinessMemberRegisterRequest request) {

        final Long businessNumber = request.getBusinessNumber();
        final String businessName = request.getBusinessName();

        // 계정 생성
        final Member member = memberRepository.save(request.toMember());

        // 회원 타입 부여
        final Role role = roleRepository.findByRoleType(request.getRoleType()).get();
        final MemberRole memberRole = new MemberRole(role, member, businessNumber, businessName);
        memberRoleRepository.save(memberRole);

        return true;
    }
}
