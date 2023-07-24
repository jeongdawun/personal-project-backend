package com.happycamper.backend.member.controller;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.authorization.JwtUtil;
import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;
import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.member.service.response.SellerInfoResponse;
import com.happycamper.backend.member.service.response.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    final private MemberService memberService;
    final JwtUtil jwtUtil;

    @PostMapping("/check-email-duplicate")
    public Boolean checkEmailDuplicate(@RequestBody CheckEmailDuplicateRequestForm requestForm) {
        Boolean isDuplicatedEmail = memberService.checkEmailDuplicate(requestForm);

        return isDuplicatedEmail;
    }

    @PostMapping("/check-businessNumber-duplicate")
    public Boolean checkEmailDuplicate(@RequestBody CheckBusinessNumberDuplicateRequestForm requestForm) {
        Boolean isDuplicatedBusinessNumber = memberService.checkBusinessNumberDuplicate(requestForm);

        return isDuplicatedBusinessNumber;
    }

    @PostMapping("/check-nickName-duplicate")
    public Boolean checkNickNameDuplicate(@RequestBody CheckNickNameDuplicateRequestForm requestForm) {
        Boolean isDuplicatedNickName = memberService.checkNickNameDuplicate(requestForm);

        return isDuplicatedNickName;
    }

    @PostMapping("/check-email-authorize")
    public Integer checkEmailAuthorize(@RequestBody CheckEmailAuthorizationRequestForm requestForm) {
        Integer authCode = memberService.checkEmailAuthorize(requestForm);

        return authCode;
    }

    @PostMapping("/signup-normal")
    public Boolean signupNormal(@RequestBody NormalMemberRegisterForm requestForm) {
        Boolean isCompleteSignupMember = memberService.normalMemberRegister(requestForm);

        return isCompleteSignupMember;
    }

    @PostMapping("/signup-business")
    public Boolean signupBusiness(@RequestBody BusinessMemberRegisterForm requestForm) {
        Boolean isCompleteSignupMember = memberService.businessMemberRegister(requestForm);

        return isCompleteSignupMember;
    }

    @PostMapping("/login")
    public Boolean login(@RequestBody MemberLoginRequestForm requestForm, HttpServletResponse response) {
        return memberService.login(requestForm, response);
    }

    @PostMapping("/auth")
    public AuthResponse authorize(HttpServletRequest request) {
        return memberService.authorize(request);
    }

    @PostMapping("/auth-userProfile")
    public UserProfileResponse getUserProfile(HttpServletRequest request) {
        return memberService.getUserProfile(request);
    }

    @PostMapping("/auth-sellerInfo")
    public SellerInfoResponse getSellerInfo(HttpServletRequest request) {
        return memberService.getSellerInfo(request);
    }

    @PostMapping("/profile-register")
    public Boolean userProfileRegister(@RequestBody UserProfileRegisterRequestForm requestForm) {
        UserProfileRegisterRequest request = requestForm.toUserProfileRegisterRequest();
        return memberService.addProfile(request);
    }

    @PostMapping("/sellerInfo-register")
    public Boolean sellerInfoRegister(@RequestBody SellerInfoRegisterRequestForm requestForm) {
        SellerInfoRegisterRequest request = requestForm.toSellerInfoRegisterRequest();
        return memberService.addSellerInfo(request);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        memberService.logout(request, response);
    }

    @GetMapping("/withdraw")
    public Boolean withdraw(HttpServletRequest request, HttpServletResponse response, @RequestParam("password") String password) {
        return memberService.withdraw(request, response, password);
    }
}
