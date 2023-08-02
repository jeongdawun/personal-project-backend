//package com.happycamper.backend.member;
//
//import com.happycamper.backend.domain.member.controller.form.BusinessMemberRegisterForm;
//import com.happycamper.backend.domain.member.controller.form.NormalMemberRegisterForm;
//import com.happycamper.backend.domain.member.controller.form.SellerInfoRegisterRequestForm;
//import com.happycamper.backend.domain.member.controller.form.UserProfileRegisterRequestForm;
//import com.happycamper.backend.domain.member.entity.RoleType;
//import com.happycamper.backend.domain.member.service.MemberService;
//import com.happycamper.backend.domain.member.service.request.SellerInfoRegisterRequest;
//import com.happycamper.backend.domain.member.service.request.UserProfileRegisterRequest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static com.happycamper.backend.domain.member.entity.RoleType.BUSINESS;
//import static com.happycamper.backend.domain.member.entity.RoleType.NORMAL;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//public class MemberTest {
//
//    @Autowired
//    private MemberService memberService;
//
//    @Test
//    @DisplayName("일반 회원 가입")
//    void 일반_회원_가입 () {
//        final String email = "test@test.com";
//        final String password = "test";
//        final RoleType roleType = NORMAL;
//
//        NormalMemberRegisterForm requestForm = new NormalMemberRegisterForm(email, password, roleType);
//        Boolean isCompleteSignUpMember = memberService.normalMemberRegister(requestForm);
//
//        assertEquals(isCompleteSignUpMember, true);
//    }
//
//    @Test
//    @DisplayName("이메일 중복 확인")
//    void 이메일_중복_확인 () {
//
//        // 가입된 메일로 중복 확인
//        final String email = "test@test.com";
//
//        Boolean isDuplicatedEmail = memberService.checkEmailDuplicate(email);
//        System.out.println("isDuplicatedEmail: " + isDuplicatedEmail);
//
//        // 중복이면 성공
//        assertEquals(isDuplicatedEmail, true);
//    }
//
//    @Test
//    @DisplayName("판매자 회원 가입")
//    void 판매자_회원_가입 () {
//        final String email = "seller@test.com";
//        final String password = "test";
//        final RoleType roleType = BUSINESS;
//        final Long businessNumber = 1111111111L;
//        final String businessName = "다운이네캠핑장";
//
//        BusinessMemberRegisterForm requestForm = new BusinessMemberRegisterForm(email, password, roleType, businessNumber, businessName);
//        Boolean isCompleteSignUpNormal = memberService.businessMemberRegister(requestForm);
//
//        assertEquals(isCompleteSignUpNormal, true);
//    }
//
//    @Test
//    @DisplayName("이메일 사용 가능 여부 확인")
//    void 이메일_사용_가능_여부_확인 () {
//        // 사용자가 입력한 이메일
//        final String userEmail = "jeongdawun33@gmail.com";
//
//        Integer authCode = memberService.checkEmailAuthorize(userEmail);
//
//        assertTrue(authCode != null);
//    }
//
//    @Test
//    @DisplayName("일반 회원의 프로필 생성")
//    void 일반_회원의_프로필_생성 () {
//        final String email = "test@test.com";
//        final String name = "토마토";
//        final Long contactNumber = null;
//        final String nickName = null;
//        final String birthday = null;
//
//        UserProfileRegisterRequestForm requestForm =
//                new UserProfileRegisterRequestForm(email, name, contactNumber, nickName, birthday);
//        UserProfileRegisterRequest request = requestForm.toUserProfileRegisterRequest();
//
//        Boolean isCompleteRegisterUserProfile = memberService.addProfile(request);
//
//        assertTrue(isCompleteRegisterUserProfile = true);
//    }
//
//    @Test
//    @DisplayName("판매자 회원의 고객센터 정보 생성")
//    void 판매자_회원의_고객센터_정보_생성 () {
//        final Long accountId = 2L;
//        final String email = "test@test.com";
//        final String city = "서울 강남구";
//        final String street = "테헤란로14길 6";
//        final String addressDetail = "6층";
//        final String zipcode = "06234";
//        final Long contactNumber = 050714002037L;
//        final String bank = "우리은행";
//        final Long accountNumber = 1002123456789L;
//
//        SellerInfoRegisterRequestForm registerRequestForm =
//                new SellerInfoRegisterRequestForm(email, city, street, addressDetail, zipcode, contactNumber, bank, accountNumber);
//        SellerInfoRegisterRequest request = registerRequestForm.toSellerInfoRegisterRequest();
//
//        Boolean isCompleteAddSellerInfo = memberService.addSellerInfo(request);
//
//        assertEquals(isCompleteAddSellerInfo, true);
//    }
//}
