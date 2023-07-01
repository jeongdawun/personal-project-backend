package com.happycamper.backend.member.service;

import com.happycamper.backend.member.controller.form.CheckEmailAuthorizationRequestForm;
import com.happycamper.backend.member.controller.form.CheckEmailDuplicateRequestForm;
import com.happycamper.backend.member.entity.userProfile.UserProfile;
import com.happycamper.backend.member.service.request.BusinessMemberRegisterRequest;
import com.happycamper.backend.member.service.request.NormalMemberRegisterRequest;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;

public interface MemberService {

    Boolean normalMemberRegister(NormalMemberRegisterRequest request);
    Boolean checkEmailDuplicate(CheckEmailDuplicateRequestForm requestForm);
    Boolean businessMemberRegister(BusinessMemberRegisterRequest toBusinessMemberRegisterRequest);
    String checkEmailAuthorize(CheckEmailAuthorizationRequestForm requestForm);
    UserProfile addProfile(Long accountId, UserProfileRegisterRequest registerRequest);
}
