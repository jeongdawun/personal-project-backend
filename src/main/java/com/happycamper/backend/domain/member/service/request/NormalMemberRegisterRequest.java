package com.happycamper.backend.domain.member.service.request;

import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.member.entity.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NormalMemberRegisterRequest {

    final private String email;
    final private String password;
    final private RoleType roleType;

    public Member toMember (String email, String password) {
        return new Member(email, password);
    }
}
