package com.happycamper.backend.member.service;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.entity.sellerInfo.SellerInfo;
import com.happycamper.backend.member.entity.userProfile.UserProfile;
import com.happycamper.backend.member.service.request.BusinessMemberRegisterRequest;
import com.happycamper.backend.member.service.request.NormalMemberRegisterRequest;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MemberService {

    Boolean normalMemberRegister(NormalMemberRegisterRequest request);
    Boolean checkEmailDuplicate(CheckEmailDuplicateRequestForm requestForm);
    Boolean businessMemberRegister(BusinessMemberRegisterRequest toBusinessMemberRegisterRequest);
//    String checkEmailAuthorize(CheckEmailAuthorizationRequestForm requestForm);
    UserProfile addProfile(Long accountId, UserProfileRegisterRequest registerRequest);
    SellerInfo addSellerInfo(Long accountId, SellerInfoRegisterRequest request);
    void login(MemberLoginRequestForm requestForm, HttpServletResponse response);
    Boolean authorize(AuthRequestForm requestForm);
}
