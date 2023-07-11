package com.happycamper.backend.member.service;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.Role;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;
import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.member.service.response.SellerInfoResponse;
import com.happycamper.backend.member.service.response.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    Member findLoginMemberByEmail(String email);
    Role findLoginMemberRoleByEmail(String email);
    Boolean normalMemberRegister(NormalMemberRegisterForm requestForm);
    Boolean checkEmailDuplicate(CheckEmailDuplicateRequestForm requestForm);
    Boolean checkBusinessNumberDuplicate(CheckBusinessNumberDuplicateRequestForm requestForm);
    Boolean checkNickNameDuplicate(CheckNickNameDuplicateRequestForm requestForm);
    Boolean businessMemberRegister(BusinessMemberRegisterForm requestForm);
    String checkEmailAuthorize(CheckEmailAuthorizationRequestForm requestForm);
    Boolean addProfile(UserProfileRegisterRequest request);
    Boolean addSellerInfo(SellerInfoRegisterRequest request);
    Boolean login(MemberLoginRequestForm requestForm, HttpServletResponse response);
    AuthResponse authorize(HttpServletRequest request);
    UserProfileResponse getUserProfile(HttpServletRequest request);
    SellerInfoResponse getSellerInfo(HttpServletRequest request);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
