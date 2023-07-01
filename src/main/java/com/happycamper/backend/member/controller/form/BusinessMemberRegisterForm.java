package com.happycamper.backend.member.controller.form;

import com.happycamper.backend.member.entity.RoleType;
import com.happycamper.backend.member.service.request.BusinessMemberRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessMemberRegisterForm {
    final private String email;
    final private String password;
    final private RoleType roleType;
    final private Long businessNumber;
    final private String businessName;

    public BusinessMemberRegisterRequest toBusinessMemberRegisterRequest () {

        return new BusinessMemberRegisterRequest(
                email, password, roleType, businessNumber, businessName);
    }
}
