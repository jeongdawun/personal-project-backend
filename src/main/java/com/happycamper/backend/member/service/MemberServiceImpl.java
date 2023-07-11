package com.happycamper.backend.member.service;

import com.happycamper.backend.member.authorization.JwtUtil;
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
import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.member.service.response.SellerInfoResponse;
import com.happycamper.backend.member.service.response.UserProfileResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    final private EmailService emailService;
    final private JwtUtil jwtUtil;
    final private RedisService redisService;
    final private PasswordEncoder passwordEncoder;

    @Value("${jwt.password}")
    private String secretKey;

    // 이메일로 회원 찾기
    @Override
    public Member findLoginMemberByEmail(String email) {
        Optional<Member> maybeMember = memberRepository.findByEmail(email);
        if(maybeMember.isEmpty()) {
            return null;
        }
        return maybeMember.get();
    }

    // 이메일로 회원 role 찾기
    @Override
    public Role findLoginMemberRoleByEmail(String email) {
        Optional<Member> maybeMember = memberRepository.findByEmail(email);
        if(maybeMember.isEmpty()) {
            return null;
        }
        Member member = maybeMember.get();

        Optional<MemberRole> maybeMemberRole = memberRoleRepository.findByMember(member);
        if(maybeMemberRole.isPresent()) {
            return maybeMemberRole.get().getRole();
        }
        return null;
    }

    // 일반 회원의 회원가입
    @Override
    public Boolean normalMemberRegister(NormalMemberRegisterForm requestForm) {
        String email = requestForm.getEmail();
        String password = passwordEncoder.encode(requestForm.getPassword());

        final NormalMemberRegisterRequest request = requestForm.toNormalMemberRegisterRequest();
        final Member member = request.toMember(email, password);

        // 계정 생성
        memberRepository.save(member);

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

    // 닉네임 중복 확인
    @Override
    public Boolean checkNickNameDuplicate(CheckNickNameDuplicateRequestForm requestForm) {
        // 존재하는 닉네임인지 확인
        Optional<UserProfile> maybeUserProfile = userProfileRepository.findByNickName(requestForm.getNickName());

        if(maybeUserProfile.isPresent()) {
            return true;
        }
        return false;
    }

    // 사업자 회원의 회원가입
    @Override
    public Boolean businessMemberRegister(BusinessMemberRegisterForm requestForm) {

        String email = requestForm.getEmail();
        String password = passwordEncoder.encode(requestForm.getPassword());

        final BusinessMemberRegisterRequest request = requestForm.toBusinessMemberRegisterRequest();
        final Member member = request.toMember(email, password);

        // 계정 생성
        memberRepository.save(member);

        final Long businessNumber = request.getBusinessNumber();
        final String businessName = request.getBusinessName();

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
    public Boolean addProfile(UserProfileRegisterRequest request) {
        final Optional<Member> maybeMember = memberRepository.findByEmail(request.getEmail());
        if (maybeMember.isEmpty()) {
            return false;
        }
        Member member = maybeMember.get();

        final Optional<UserProfile> maybeUserProfile = userProfileRepository.findUserProfileByMember(member);

        if (maybeUserProfile.isEmpty()) {
            UserProfile userProfile =
                    new UserProfile(request.getName(), request.getContactNumber(), request.getNickName(), request.getBirthday(), member);
            userProfileRepository.save(userProfile);
            return true;
        }

        UserProfile userProfile = maybeUserProfile.get();
        userProfile.setName(request.getName());
        userProfile.setContactNumber(request.getContactNumber());
        userProfile.setNickName(request.getNickName());
        userProfile.setBirthday(request.getBirthday());

        System.out.println("UserProfile: " + userProfile);

        userProfileRepository.save(userProfile);
        return true;
    }

    // 판매자 회원의 고객센터 정보 생성
    @Override
    public Boolean addSellerInfo(SellerInfoRegisterRequest request) {
        final Optional<Member> maybeMember = memberRepository.findByEmail(request.getEmail());
        if (maybeMember.isEmpty()) {
            return false;
        }
        Member member = maybeMember.get();

        final Optional<SellerInfo> maybeSellerInfo = sellerInfoRepository.findSellerInfoByMember(member);

        if (maybeSellerInfo.isEmpty()) {
            SellerInfo sellerInfo =
                    new SellerInfo(request.getAddress(), request.getContactNumber(), request.getBank(), request.getAccountNumber(), member);
            System.out.println("궁금하다: " + sellerInfo);
            sellerInfoRepository.save(sellerInfo);
            return true;
        }

        SellerInfo sellerInfo = maybeSellerInfo.get();
        sellerInfo.setAddress(request.getAddress());
        sellerInfo.setContactNumber(request.getContactNumber());
        sellerInfo.setBank(request.getBank());
        sellerInfo.setAccountNumber(request.getAccountNumber());

        System.out.println("SellerInfo: " + sellerInfo);

        sellerInfoRepository.save(sellerInfo);
        return true;
    }

    // 회원의 로그인
    @Override
    public Boolean login(MemberLoginRequestForm requestForm, HttpServletResponse response) {
        Optional<Member> maybeMember = memberRepository.findByEmail(requestForm.getEmail());

        if(maybeMember.isPresent()) {
            if(passwordEncoder.matches(requestForm.getPassword(), maybeMember.get().getPassword())) {

                final Member member = maybeMember.get();

                String accessToken = jwtUtil.generateToken(requestForm.getEmail(), secretKey, 6 * 60 * 60 * 1000);
                String refreshToken = jwtUtil.generateToken(requestForm.getEmail(), secretKey, 2 * 7 * 24 * 60 * 60 * 1000);
                redisService.setKeyAndValue(refreshToken, member.getId());

                System.out.println("AccessToken: " + accessToken);
                System.out.println("RefreshToken: " + refreshToken);

                Cookie assessCookie = jwtUtil.generateCookie("AccessToken", accessToken, 60 * 60 * 6, false);
                Cookie refreshCookie = jwtUtil.generateCookie("RefreshToken", refreshToken, 60 * 60 * 24 * 14, true);

                response.addCookie(assessCookie);
                response.addCookie(refreshCookie);

                return true;
            }
        }
        return false;
    }

    // 토큰 검증 후 권한 확인
    @Override
    public AuthResponse authorize(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("AccessToken")) {

                    String accessToken = cookie.getValue();
                    String email = jwtUtil.getEmail(accessToken, secretKey);

                    Optional<Member> maybeMember = memberRepository.findByEmail(email);
                    if(maybeMember.isPresent()) {
                        Optional<MemberRole> maybeMemberRole = memberRoleRepository.findByMember(maybeMember.get());
                        if(maybeMemberRole.isPresent()) {
                            return new AuthResponse(email, maybeMemberRole.get().getRole().getRoleType().toString());
                        }
                    }
                }
            }
        }
        return null;
    }

    // 일반 회원 프로필 가져오기
    @Override
    public UserProfileResponse getUserProfile(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("AccessToken")) {

                    String accessToken = cookie.getValue();
                    String email = jwtUtil.getEmail(accessToken, secretKey);

                    Optional<Member> maybeMember = memberRepository.findByEmail(email);
                    if(maybeMember.isPresent()) {

                        Optional<UserProfile> maybeUserProfile = userProfileRepository.findUserProfileByMember(maybeMember.get());
                        if(maybeUserProfile.isPresent()) {

                        UserProfile userProfile = maybeUserProfile.get();
                        UserProfileResponse response =
                                new UserProfileResponse(
                                        email,
                                        userProfile.getName(),
                                        userProfile.getContactNumber(),
                                        userProfile.getNickName(),
                                        userProfile.getBirthday());

                                return response;
                        }
                        if(maybeUserProfile.isEmpty()) {

                            UserProfileResponse response = new UserProfileResponse(email);
                            return response;
                        }
                    }
                }
            }
        }
        return null;
    }

    // 판매자 회원 고객센터 정보 가져오기
    @Override
    public SellerInfoResponse getSellerInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("AccessToken")) {

                    String accessToken = cookie.getValue();
                    String email = jwtUtil.getEmail(accessToken, secretKey);

                    Optional<Member> maybeMember = memberRepository.findByEmail(email);
                    if(maybeMember.isPresent()) {

                        Optional<SellerInfo> maybeSellerInfo = sellerInfoRepository.findSellerInfoByMember(maybeMember.get());
                        Optional<MemberRole> maybeMemberRole = memberRoleRepository.findByMember(maybeMember.get());

                        if(maybeSellerInfo.isPresent() && maybeMemberRole.isPresent()) {
                            SellerInfo sellerInfo = maybeSellerInfo.get();
                            SellerInfoResponse response =
                                    new SellerInfoResponse(
                                            email,
                                            maybeMemberRole.get().getBusinessNumber(),
                                            maybeMemberRole.get().getBusinessName(),
                                            sellerInfo.getAddress().getCity(),
                                            sellerInfo.getAddress().getStreet(),
                                            sellerInfo.getAddress().getAddressDetail(),
                                            sellerInfo.getAddress().getZipcode(),
                                            sellerInfo.getContactNumber(),
                                            sellerInfo.getBank(),
                                            sellerInfo.getAccountNumber());

                            return response;
                        }
                        if(maybeSellerInfo.isEmpty()) {
                            if(maybeMemberRole.isPresent()) {
                                SellerInfoResponse response =
                                        new SellerInfoResponse(
                                                email,
                                                maybeMemberRole.get().getBusinessNumber(),
                                                maybeMemberRole.get().getBusinessName());
                                return response;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    // 로그아웃
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("AccessToken")) {

                    String accessToken = cookie.getValue();
                    System.out.println("클라이언트에서 가져온 accessToken: " + accessToken);

                    Cookie assessCookie = jwtUtil.generateCookie("AccessToken", null, 0, false);

                    response.addCookie(assessCookie);
                }
                if(cookie.getName().equals("RefreshToken")) {
                    String refreshToken = cookie.getValue();
                    System.out.println("클라이언트에서 가져온 refreshToken: " + refreshToken);

                    Cookie refreshCookie = jwtUtil.generateCookie("RefreshToken", null, 0, true);
                    response.addCookie(refreshCookie);

                    redisService.deleteByKey(refreshToken);
                }
            }
        }
    }
}
