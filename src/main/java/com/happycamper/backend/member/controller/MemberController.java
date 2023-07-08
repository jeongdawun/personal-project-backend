package com.happycamper.backend.member.controller;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;
import com.happycamper.backend.member.service.response.SellerInfoResponse;
import com.happycamper.backend.member.service.response.UserProfileResponse;
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
    public String checkEmailAuthorize(@RequestBody CheckEmailAuthorizationRequestForm requestForm) {
        String authCode = memberService.checkEmailAuthorize(requestForm);

        return authCode;
    }

    @PostMapping("/signup-normal")
    public Boolean signupNormal(@RequestBody NormalMemberRegisterForm requestForm) {
        Boolean isCompleteSignupMember = memberService.normalMemberRegister(requestForm.toNormalMemberRegisterRequest());

        return isCompleteSignupMember;
    }

    @PostMapping("/signup-business")
    public Boolean signupBusiness(@RequestBody BusinessMemberRegisterForm requestForm) {
        Boolean isCompleteSignupMember = memberService.businessMemberRegister(requestForm.toBusinessMemberRegisterRequest());

        return isCompleteSignupMember;
    }

    @PostMapping("/login")
    public void login(@RequestBody MemberLoginRequestForm requestForm, HttpServletResponse response) {
        memberService.login(requestForm, response);
    }

    @PostMapping("/auth")
    public String authorize(@RequestBody AuthRequestForm requestForm) {
        return memberService.authorize(requestForm);
    }

    @PostMapping("/auth-userProfile")
    public UserProfileResponse authorizeForUserProfile(@RequestBody AuthRequestForm requestForm) {
        return memberService.authorizeForUserProfile(requestForm);
    }

    @PostMapping("/auth-sellerInfo")
    public SellerInfoResponse authorizeForSellerInfo(@RequestBody AuthRequestForm requestForm) {
        return memberService.authorizeForSellerInfo(requestForm);
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
    public void logout(@RequestBody AuthRequestForm requestForm) {
        memberService.logout(requestForm);
    }
}
