package com.happycamper.backend.domain.member.controller.form;

import com.happycamper.backend.domain.member.entity.RoleType;
import com.happycamper.backend.domain.member.service.request.NormalMemberRegisterRequest;
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
