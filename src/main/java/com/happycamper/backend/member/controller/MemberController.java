package com.happycamper.backend.member.controller;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.service.MemberService;
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
    public Boolean authorize(@RequestBody AuthRequestForm requestForm) {
        return memberService.authorize(requestForm);
    }
}
