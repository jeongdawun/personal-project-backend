package com.happycamper.backend.member;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.entity.RoleType;
import com.happycamper.backend.member.entity.userProfile.UserProfile;
import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.happycamper.backend.member.entity.RoleType.BUSINESS;
import static com.happycamper.backend.member.entity.RoleType.NORMAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    @DisplayName("판매자 회원 가입")
    void 판매자_회원_가입 () {
        final String email = "seller@test.com";
        final String password = "test";
        final RoleType roleType = BUSINESS;
        final Long businessNumber = 1111111111L;
        final String businessName = "다운이네캠핑장";

        BusinessMemberRegisterForm requestForm = new BusinessMemberRegisterForm(email, password, roleType, businessNumber, businessName);
        Boolean isCompleteSignUpNormal = memberService.businessMemberRegister(requestForm.toBusinessMemberRegisterRequest());

        assertEquals(isCompleteSignUpNormal, true);
    }

    @Test
    @DisplayName("이메일 사용 가능 여부 확인")
    void 이메일_사용_가능_여부_확인 () {
        // 사용자가 입력한 이메일
        final String userEmail = "jeongdawun33@gmail.com";

        CheckEmailAuthorizationRequestForm requestForm = new CheckEmailAuthorizationRequestForm(userEmail);
        String authCode = memberService.checkEmailAuthorize(requestForm);

        assertTrue(authCode != null);
    }

    @Test
    @DisplayName("일반 회원의 프로필 생성")
    void 일반_회원의_프로필_생성 () {
        final Long accountId = 1L;
        final String name = "정다운";
        final Long contactNumber = null;
        final String nickName = null;
        final String birthday = null;

        UserProfileRegisterRequestForm requestForm =
                new UserProfileRegisterRequestForm(name, contactNumber, nickName, birthday);
        UserProfileRegisterRequest registerRequest = requestForm.toUserProfileRegisterRequest();

        UserProfile userProfile = memberService.addProfile(accountId, registerRequest);

        assertEquals(userProfile.getName(), name);
    }
}
