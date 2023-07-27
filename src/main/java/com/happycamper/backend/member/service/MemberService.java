package com.happycamper.backend.member.service;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.Role;
import com.happycamper.backend.member.service.request.MemberLoginRequest;
import com.happycamper.backend.member.service.request.MemberPasswordCheckRequest;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;
import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.member.service.response.SellerInfoResponse;
import com.happycamper.backend.member.service.response.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    Member findMemberByEmail(String email);
    Role findLoginMemberRoleByEmail(String email);
    Boolean normalMemberRegister(NormalMemberRegisterForm requestForm);
    Boolean checkEmailDuplicate(String email);
    Boolean checkBusinessNumberDuplicate(Long businessNumber);
    Boolean checkNickNameDuplicate(String nickName);
    Boolean businessMemberRegister(BusinessMemberRegisterForm requestForm);
    Integer checkEmailAuthorize(String email);
    Boolean addProfile(UserProfileRegisterRequest request);
    Boolean addSellerInfo(SellerInfoRegisterRequest request);
    Boolean login(MemberLoginRequest request, HttpServletResponse response);
    AuthResponse authorize(HttpServletRequest request);
    UserProfileResponse getUserProfile(HttpServletRequest request);
    SellerInfoResponse getSellerInfo(HttpServletRequest request);
    Boolean logout(HttpServletRequest request, HttpServletResponse response);
    Boolean withdrawal(HttpServletRequest request, HttpServletResponse response, MemberPasswordCheckRequest password);
}
