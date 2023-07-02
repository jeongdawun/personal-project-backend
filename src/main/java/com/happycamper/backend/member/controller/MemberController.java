package com.happycamper.backend.member.controller;

import com.happycamper.backend.member.controller.form.BusinessMemberRegisterForm;
import com.happycamper.backend.member.controller.form.CheckEmailAuthorizationRequestForm;
import com.happycamper.backend.member.controller.form.CheckEmailDuplicateRequestForm;
import com.happycamper.backend.member.controller.form.NormalMemberRegisterForm;
import com.happycamper.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
