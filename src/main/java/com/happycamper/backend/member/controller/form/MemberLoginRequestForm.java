package com.happycamper.backend.member.controller.form;

import com.happycamper.backend.member.service.request.MemberLoginRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberLoginRequestForm {
    final private String email;
    final private String password;

    public MemberLoginRequest toMemberLoginRequest() {
        return new MemberLoginRequest(email, password);
    }
}
