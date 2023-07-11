package com.happycamper.backend.member.service.request;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessMemberRegisterRequest {
    final private String email;
    final private String password;
    final private RoleType roleType;
    final private Long businessNumber;
    final private String businessName;

    public Member toMember (String email, String password) {
        return new Member(email, password);
    }
}
