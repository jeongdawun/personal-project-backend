package com.happycamper.backend.member.controller.form;

import com.happycamper.backend.member.entity.RoleType;
import com.happycamper.backend.member.service.request.NormalMemberRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NormalMemberRegisterForm {

    final private String email;
    final private String password;
    final private RoleType roleType;

    public NormalMemberRegisterRequest toNormalMemberRegisterRequest () {

        return new NormalMemberRegisterRequest(
                email, password, roleType);
    }
}
