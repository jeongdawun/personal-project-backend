package com.happycamper.backend.member;

import com.happycamper.backend.member.controller.form.CheckEmailDuplicateRequestForm;
import com.happycamper.backend.member.controller.form.NormalMemberRegisterForm;
import com.happycamper.backend.member.entity.RoleType;
import com.happycamper.backend.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.happycamper.backend.member.entity.RoleType.NORMAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("일반 회원 가입")
    void 일반_회원_가입 () {
        final String email = "test@test.com";
        final String password = "test";
        final RoleType roleType = NORMAL;

        NormalMemberRegisterForm requestForm = new NormalMemberRegisterForm(email, password, roleType);
        Boolean isCompleteSignUpMember = memberService.normalMemberRegister(requestForm.toNormalMemberRegisterRequest());

        assertEquals(isCompleteSignUpMember, true);
    }

    @Test
    @DisplayName("이메일 중복 확인")
    void 이메일_중복_확인 () {

        // 가입된 메일로 중복 확인
        final String email = "test@test.com";

        CheckEmailDuplicateRequestForm requestForm = new CheckEmailDuplicateRequestForm(email);
        Boolean isDuplicatedEmail = memberService.checkEmailDuplicate(requestForm);
        System.out.println("isDuplicatedEmail: " + isDuplicatedEmail);

        // 중복이면 성공
        assertEquals(isDuplicatedEmail, true);
    }
}
