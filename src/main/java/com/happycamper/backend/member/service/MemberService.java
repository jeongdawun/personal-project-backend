package com.happycamper.backend.member.service;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.service.request.BusinessMemberRegisterRequest;
import com.happycamper.backend.member.service.request.NormalMemberRegisterRequest;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;
import com.happycamper.backend.member.service.response.SellerInfoResponse;
import com.happycamper.backend.member.service.response.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    Boolean normalMemberRegister(NormalMemberRegisterRequest request);
    Boolean checkEmailDuplicate(CheckEmailDuplicateRequestForm requestForm);
    Boolean checkBusinessNumberDuplicate(CheckBusinessNumberDuplicateRequestForm requestForm);
    Boolean checkNickNameDuplicate(CheckNickNameDuplicateRequestForm requestForm);
    Boolean businessMemberRegister(BusinessMemberRegisterRequest toBusinessMemberRegisterRequest);
    String checkEmailAuthorize(CheckEmailAuthorizationRequestForm requestForm);
    Boolean addProfile(UserProfileRegisterRequest request);
    Boolean addSellerInfo(SellerInfoRegisterRequest request);
    void login(MemberLoginRequestForm requestForm, HttpServletResponse response);
    String authorize(AuthRequestForm requestForm);
    UserProfileResponse authorizeForUserProfile(AuthRequestForm requestForm);
    SellerInfoResponse authorizeForSellerInfo(AuthRequestForm requestForm);
    void logout(AuthRequestForm requestForm);
    void createAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response);
}
