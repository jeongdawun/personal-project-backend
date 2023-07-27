package com.happycamper.backend.member.controller.form;

import com.happycamper.backend.member.service.request.MemberPasswordCheckRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberPasswordCheckRequestForm {
    private String password;

    public MemberPasswordCheckRequest toMemberPasswordCheckRequest() {
        return new MemberPasswordCheckRequest(password);
    }
}
