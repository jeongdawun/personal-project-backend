package com.happycamper.backend.member.service;

import com.happycamper.backend.member.controller.form.*;
import com.happycamper.backend.member.entity.*;
import com.happycamper.backend.member.entity.sellerInfo.SellerInfo;
import com.happycamper.backend.member.entity.userProfile.UserProfile;
import com.happycamper.backend.member.repository.MemberRepository;
import com.happycamper.backend.member.repository.MemberRoleRepository;
import com.happycamper.backend.member.repository.RoleRepository;
import com.happycamper.backend.member.repository.sellerInfo.SellerInfoRepository;
import com.happycamper.backend.member.repository.userProfile.UserProfileRepository;
import com.happycamper.backend.member.service.request.BusinessMemberRegisterRequest;
import com.happycamper.backend.member.service.request.NormalMemberRegisterRequest;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import com.happycamper.backend.member.service.request.UserProfileRegisterRequest;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    final private MemberRepository memberRepository;
    final private MemberRoleRepository memberRoleRepository;
    final private UserProfileRepository userProfileRepository;
    final private SellerInfoRepository sellerInfoRepository;
    final private RoleRepository roleRepository;
    final EmailService emailService;
    final JwtTokenService jwtTokenService;
    final RedisService redisService;

    // 일반 회원의 회원가입
    @Override
    public Boolean normalMemberRegister(NormalMemberRegisterRequest request) {
        // 계정 생성
        final Member member = memberRepository.save(request.toMember());

        // 회원 타입 부여
        final Role role = roleRepository.findByRoleType(request.getRoleType()).get();
        final MemberRole memberRole = new MemberRole(role, member);
        memberRoleRepository.save(memberRole);

        return true;
    }

    // 이메일 중복 확인
    @Override
    public Boolean checkEmailDuplicate(CheckEmailDuplicateRequestForm requestForm) {
        // 존재하는 계정인지 확인
        Optional<Member> maybeMember = memberRepository.findByEmail(requestForm.getEmail());

        if(maybeMember.isPresent()) {
            return true;
        }
        return false;
    }

    // 사업자 번호 중복 확인
    @Override
    public Boolean checkBusinessNumberDuplicate(CheckBusinessNumberDuplicateRequestForm requestForm) {
        // 존재하는 사업자 번호인지 확인
        Optional<MemberRole> maybeMemberRole = memberRoleRepository.findByBusinessNumber(requestForm.getBusinessNumber());

        if(maybeMemberRole.isPresent()) {
            return true;
        }
        return false;
    }

    // 사업자 회원의 회원가입
    @Override
    public Boolean businessMemberRegister(BusinessMemberRegisterRequest request) {

        final Long businessNumber = request.getBusinessNumber();
        final String businessName = request.getBusinessName();

        // 계정 생성
        final Member member = memberRepository.save(request.toMember());

        // 회원 타입 부여
        final Role role = roleRepository.findByRoleType(request.getRoleType()).get();
        final MemberRole memberRole = new MemberRole(role, member, businessNumber, businessName);
        memberRoleRepository.save(memberRole);

        return true;
    }

    // 이메일 사용 가능 여부 확인
    @Override
    public String checkEmailAuthorize(CheckEmailAuthorizationRequestForm requestForm) {
        Email email = emailService.createEmail(requestForm.getEmail());
        String authCode = emailService.sendEmail(email);
        return authCode;
    }

    // 일반 회원의 회원 프로필 생성
    @Override
    public UserProfile addProfile(Long accountId, UserProfileRegisterRequest request) {
        final Optional<Member> maybeMember = memberRepository.findById(accountId);
        if (maybeMember.isEmpty()) {
            return null;
        }
        Member member = maybeMember.get();

        final Optional<UserProfile> maybeUserProfile = userProfileRepository.findUserProfileByMember(member);

        if (maybeUserProfile.isEmpty()) {
            UserProfile userProfile =
                    new UserProfile(request.getName(), request.getContactNumber(), request.getNickName(), request.getBirthday(), member);
            return userProfileRepository.save(userProfile);
        }

        UserProfile userProfile = maybeUserProfile.get();
        userProfile.setName(request.getName());
        userProfile.setContactNumber(request.getContactNumber());
        userProfile.setNickName(request.getNickName());
        userProfile.setBirthday(request.getBirthday());

        System.out.println("UserProfile: " + userProfile);

        return userProfileRepository.save(userProfile);
    }

    // 판매자 회원의 고객센터 정보 생성
    @Override
    public SellerInfo addSellerInfo(Long accountId, SellerInfoRegisterRequest request) {
        final Optional<Member> maybeMember = memberRepository.findById(accountId);
        if (maybeMember.isEmpty()) {
            return null;
        }
        Member member = maybeMember.get();

        final Optional<SellerInfo> maybeSellerInfo = sellerInfoRepository.findSellerInfoByMember(member);

        if (maybeSellerInfo.isEmpty()) {
            SellerInfo sellerInfo =
                    new SellerInfo(request.getAddress(), request.getContactNumber(), request.getBank(), request.getAccountNumber(), member);
            return sellerInfoRepository.save(sellerInfo);
        }

        SellerInfo sellerInfo = maybeSellerInfo.get();
        sellerInfo.setAddress(request.getAddress());
        sellerInfo.setContactNumber(request.getContactNumber());
        sellerInfo.setBank(request.getBank());
        sellerInfo.setAccountNumber(request.getAccountNumber());

        System.out.println("SellerInfo: " + sellerInfo);

        return sellerInfoRepository.save(sellerInfo);
    }

    // 회원의 로그인
    @Override
    public void login(MemberLoginRequestForm requestForm, HttpServletResponse response) {
        Optional<Member> maybeMember = memberRepository.findByEmail(requestForm.getEmail());

        if(maybeMember.isPresent()) {
            if(requestForm.getPassword().equals(maybeMember.get().getPassword())) {

                final Member member = maybeMember.get();

                String accessToken = jwtTokenService.generateAccessToken(requestForm.getEmail());
                String refreshToken = jwtTokenService.generateRefreshToken(requestForm.getEmail());
                redisService.setKeyAndValue(refreshToken, member.getId());

                String tokens = "Bearer " + accessToken + " " + refreshToken;

                response.setHeader("Authorization", tokens);
                System.out.println("accessToken + refreshToken: " + tokens);
            }
        }
    }

    // 사용자 인증
    @Override
    public String authorize(AuthRequestForm requestForm) {
        System.out.println("검증할 토큰: " + requestForm.getAuthorizationHeader());

        String token = requestForm.getAuthorizationHeader();
        Claims claims = jwtTokenService.parseJwtToken(token);
        System.out.println("Claims: " + claims);
        return claims.getSubject();
    }
}
