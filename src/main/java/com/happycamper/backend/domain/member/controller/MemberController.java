package com.happycamper.backend.domain.member.controller;

import com.happycamper.backend.domain.member.controller.form.*;
import com.happycamper.backend.domain.member.service.MemberService;
import com.happycamper.backend.domain.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.domain.member.service.request.UserProfileRegisterRequest;
import com.happycamper.backend.domain.member.service.response.SellerInfoResponse;
import com.happycamper.backend.domain.member.service.response.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    final private MemberService memberService;

    // 이메일 중복 확인(완료)
    @GetMapping("/check-email-duplicate")
    public Boolean checkEmailDuplicate(@RequestParam("email") String email) {

        return memberService.checkEmailDuplicate(email);
    }

    // 사업자 번호 중복 확인(완료)
    @GetMapping("/check-businessNumber-duplicate")
    public Boolean checkBusinessNumberDuplicate(@RequestParam("businessNumber") Long businessNumber) {

        return memberService.checkBusinessNumberDuplicate(businessNumber);
    }

    // 닉네임 중복 확인(완료)
    @GetMapping("/check-nickName-duplicate")
    public Boolean checkNickNameDuplicate(@RequestParam("nickName") String nickName) {

        return memberService.checkNickNameDuplicate(nickName);
    }

    // 이메일 인증(완료)
    @GetMapping("/check-email-authorize")
    public Integer checkEmailAuthorize(@RequestParam("email") String email) {

        return memberService.checkEmailAuthorize(email);
    }

    // 일반 회원의 회원가입(완료)
    @PostMapping("/signup-normal")
    public Boolean signupNormal(@RequestBody NormalMemberRegisterForm requestForm) {

        return memberService.normalMemberRegister(requestForm);
    }

    // 사업자 회원의 회원가입(완료)
    @PostMapping("/signup-business")
    public Boolean signupBusiness(@RequestBody BusinessMemberRegisterForm requestForm) {

        return memberService.businessMemberRegister(requestForm);
    }

    // 로그인(완료)
    @PostMapping("/login")
    public Boolean login(@RequestBody MemberLoginRequestForm requestForm, HttpServletResponse response) {
        return memberService.login(requestForm.toMemberLoginRequest(), response);
    }

    // 로그아웃(완료)
    @GetMapping("/logout")
    public Boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return memberService.logout(request, response);
    }

    // 일반 회원 프로필 가져오기(완료)
    @GetMapping("/auth-userProfile")
    public UserProfileResponse getUserProfile(Authentication authentication) {
        return memberService.getUserProfile(authentication);
    }

    // 판매자 회원 고객센터 정보 가져오기(완료)
    @GetMapping("/auth-sellerInfo")
    public SellerInfoResponse getSellerInfo(Authentication authentication) {
        return memberService.getSellerInfo(authentication);
    }

    // 일반 회원 프로필 등록하기(완료)
    @PostMapping("/profile-register")
    public Boolean userProfileRegister(@RequestBody UserProfileRegisterRequestForm requestForm, Authentication authentication) {
        UserProfileRegisterRequest request = requestForm.toUserProfileRegisterRequest();
        return memberService.addProfile(request, authentication);
    }

    // 판매자 회원 고객센터 정보 등록하기(완료)
    @PostMapping("/sellerInfo-register")
    public Boolean sellerInfoRegister(@RequestBody SellerInfoRegisterRequestForm requestForm, Authentication authentication) {
        SellerInfoRegisterRequest request = requestForm.toSellerInfoRegisterRequest();
        return memberService.addSellerInfo(request, authentication);
    }

    // 일반 회원의 회원탙퇴(완료)
    @PostMapping("/withdrawal")
    public Boolean withdrawal(HttpServletRequest request, HttpServletResponse response, @RequestBody MemberPasswordCheckRequestForm requestForm) {
        return memberService.withdrawal(request, response, requestForm.toMemberPasswordCheckRequest());
    }

    // 회원의 권한 확인
    @GetMapping("/check-role")
    public String checkRole(Authentication authentication) {
        return authentication.getAuthorities().toString();
    }
}
