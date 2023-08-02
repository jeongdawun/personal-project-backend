package com.happycamper.backend.domain.member.service;

import com.happycamper.backend.domain.member.controller.form.BusinessMemberRegisterForm;
import com.happycamper.backend.domain.member.controller.form.NormalMemberRegisterForm;
import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.member.entity.Role;
import com.happycamper.backend.domain.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.domain.member.service.response.SellerInfoResponse;
import com.happycamper.backend.domain.member.service.response.UserProfileResponse;
import com.happycamper.backend.domain.member.service.request.MemberLoginRequest;
import com.happycamper.backend.domain.member.service.request.MemberPasswordCheckRequest;
import com.happycamper.backend.domain.member.service.request.UserProfileRegisterRequest;
import com.happycamper.backend.domain.member.service.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    String extractEmailByCookie(HttpServletRequest request);
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
