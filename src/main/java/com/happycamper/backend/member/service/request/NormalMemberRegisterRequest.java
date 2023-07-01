package com.happycamper.backend.member.service.request;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NormalMemberRegisterRequest {

    final private String email;
    final private String password;
    final private RoleType roleType;

    public Member toMember () {
        return new Member(email, password);
    }
}
