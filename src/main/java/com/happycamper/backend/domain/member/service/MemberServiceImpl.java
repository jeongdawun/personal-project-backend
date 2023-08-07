package com.happycamper.backend.domain.member.service;

import com.happycamper.backend.authorization.JwtUtil;
import com.happycamper.backend.domain.member.controller.form.BusinessMemberRegisterForm;
import com.happycamper.backend.domain.member.controller.form.NormalMemberRegisterForm;
import com.happycamper.backend.domain.member.entity.Email;
import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.member.entity.MemberRole;
import com.happycamper.backend.domain.member.entity.Role;
import com.happycamper.backend.domain.member.repository.MemberRepository;
import com.happycamper.backend.domain.member.repository.MemberRoleRepository;
import com.happycamper.backend.domain.member.service.request.*;
import com.happycamper.backend.domain.member.service.response.SellerInfoResponse;
import com.happycamper.backend.domain.member.service.response.UserProfileResponse;
import com.happycamper.backend.domain.reservation.entity.Reservation;
import com.happycamper.backend.domain.reservation.entity.ReservationStatus;
import com.happycamper.backend.domain.reservation.entity.Status;
import com.happycamper.backend.domain.reservation.repository.ReservationRepository;
import com.happycamper.backend.domain.reservation.repository.ReservationStatusRepository;
import com.happycamper.backend.domain.member.entity.sellerInfo.SellerInfo;
import com.happycamper.backend.domain.member.entity.userProfile.UserProfile;
import com.happycamper.backend.domain.member.repository.RoleRepository;
import com.happycamper.backend.domain.member.repository.sellerInfo.SellerInfoRepository;
import com.happycamper.backend.domain.member.repository.userProfile.UserProfileRepository;
import com.happycamper.backend.domain.member.service.response.AuthResponse;
import com.happycamper.backend.utility.random.CustomRandom;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
    final private ReservationRepository reservationRepository;
    final private ReservationStatusRepository reservationStatusRepository;
    final private EmailService emailService;
    final private RedisService redisService;
    final private PasswordEncoder passwordEncoder;

    @Value("${jwt.password}")
    private String secretKey;

    // 쿠키로 회원 이메일 찾기(완료)
    @Override
    public String extractEmailByCookie(HttpServletRequest request) {
        String accessToken = JwtUtil.extractTokenByCookie(request, "AccessToken");
        return JwtUtil.getEmail(accessToken, secretKey);
    }

    // 이메일로 회원 찾기(완료)
    @Override
    public Member findMemberByEmail(String email) {
        Optional<Member> maybeMember = memberRepository.findByEmail(email);
        if(maybeMember.isEmpty()) {
            log.info("Member is Empty");
            return null;
        }
        return maybeMember.get();
    }

    // 이메일로 회원 role 찾기(완료)
    @Override
    public Role findLoginMemberRoleByEmail(String email) {
        final Member member = findMemberByEmail(email);

        Optional<MemberRole> maybeMemberRole = memberRoleRepository.findByMember(member);
        if(maybeMemberRole.isPresent()) {
            return maybeMemberRole.get().getRole();
        }
        return null;
    }

    // 이메일 중복 확인(완료)
    // false : 중복이 아님, true : 중복
    @Override
    public Boolean checkEmailDuplicate(String email) {
        final Member member = findMemberByEmail(email);

        if(member != null) {
            if(member.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

    // 사업자 번호 중복 확인(완료)
    // false : 중복이 아님, true : 중복
    @Override
    public Boolean checkBusinessNumberDuplicate(Long businessNumber) {
        // 존재하는 사업자 번호인지 확인
        Optional<MemberRole> maybeMemberRole = memberRoleRepository.findByBusinessNumber(businessNumber);

        if(maybeMemberRole.isPresent()) {
            if(maybeMemberRole.get().getBusinessNumber().equals(businessNumber)) {
                return true;
            }
        }
        return false;
    }

    // 닉네임 중복 확인(완료)
    // false : 중복이 아님, true : 중복
    @Override
    public Boolean checkNickNameDuplicate(String nickName) {
        // 존재하는 닉네임인지 확인
        Optional<UserProfile> maybeUserProfile = userProfileRepository.findByNickName(nickName);

        if(maybeUserProfile.isPresent()) {
            if(maybeUserProfile.get().getNickName().equals(nickName)) {
                return true;
            }
        }
        return false;
    }

    // 이메일 사용 가능 여부 확인
    @Override
    public Integer checkEmailAuthorize(String email) {
        int randomCode = CustomRandom.generateNumber(0000, 9999);
        Email sendEmail = emailService.createEmail(email, randomCode);
        emailService.sendEmail(sendEmail);

        return randomCode;
    }

    // 일반 회원의 회원가입(완료)
    @Override
    public Boolean normalMemberRegister(NormalMemberRegisterForm requestForm) {
        String email = requestForm.getEmail();
        String password = passwordEncoder.encode(requestForm.getPassword());

        final NormalMemberRegisterRequest request = requestForm.toNormalMemberRegisterRequest();
        final Member member = request.toMember(email, password);

        // Member 생성 후 DB에 저장
        memberRepository.save(member);

        // Member Role 부여
        final Role role = roleRepository.findByRoleType(request.getRoleType()).get();
        final MemberRole memberRole = new MemberRole(role, member);
        memberRoleRepository.save(memberRole);

        return true;
    }

    // 사업자 회원의 회원가입(완료)
    @Override
    public Boolean businessMemberRegister(BusinessMemberRegisterForm requestForm) {
        String email = requestForm.getEmail();
        String password = passwordEncoder.encode(requestForm.getPassword());

        final BusinessMemberRegisterRequest request = requestForm.toBusinessMemberRegisterRequest();
        final Member member = request.toMember(email, password);

        // Member 생성 후 DB에 저장
        memberRepository.save(member);

        final Long businessNumber = request.getBusinessNumber();
        final String businessName = request.getBusinessName();

        // Member Role 부여
        final Role role = roleRepository.findByRoleType(request.getRoleType()).get();
        final MemberRole memberRole = new MemberRole(role, member, businessNumber, businessName);
        memberRoleRepository.save(memberRole);

        return true;
    }

    // 일반 회원 프로필 등록하기(완료)
    @Override
    public Boolean addProfile(UserProfileRegisterRequest request) {
        final Member member = findMemberByEmail(request.getEmail());
        if (member == null) {
            return false;
        }

        final Optional<UserProfile> maybeUserProfile = userProfileRepository.findUserProfileByMember(member);

        if(maybeUserProfile.isEmpty()) {
            UserProfile userProfile =
                    new UserProfile(
                            request.getName(),
                            request.getContactNumber(),
                            request.getNickName(),
                            request.getBirthday(),
                            member);
            userProfileRepository.save(userProfile);

            return true;
        }

        if(maybeUserProfile.isPresent()) {
            UserProfile userProfile = maybeUserProfile.get();
            userProfile.setName(request.getName());
            userProfile.setContactNumber(request.getContactNumber());
            userProfile.setNickName(request.getNickName());
            userProfile.setBirthday(request.getBirthday());

            userProfileRepository.save(userProfile);

            return true;
        }
        return false;
    }

    // 판매자 회원 고객센터 정보 등록하기(완료)
    @Override
    public Boolean addSellerInfo(SellerInfoRegisterRequest request) {
        final Member member = findMemberByEmail(request.getEmail());
        if (member == null) {
            return false;
        }

        final Optional<SellerInfo> maybeSellerInfo = sellerInfoRepository.findSellerInfoByMember(member);

        if(maybeSellerInfo.isEmpty()) {
            SellerInfo sellerInfo =
                    new SellerInfo(
                            request.getAddress(),
                            request.getContactNumber(),
                            request.getBank(),
                            request.getAccountNumber(),
                            member);
            sellerInfoRepository.save(sellerInfo);

            return true;
        }

        if(maybeSellerInfo.isPresent()) {
            SellerInfo sellerInfo = maybeSellerInfo.get();
            sellerInfo.setAddress(request.getAddress());
            sellerInfo.setContactNumber(request.getContactNumber());
            sellerInfo.setBank(request.getBank());
            sellerInfo.setAccountNumber(request.getAccountNumber());

            sellerInfoRepository.save(sellerInfo);

            return true;
        }
        return false;
    }

    // 로그인(완료)
    @Override
    public Boolean login(MemberLoginRequest request, HttpServletResponse response) {
        final Member member = findMemberByEmail(request.getEmail());

        if(member == null) {
            return false;
        }

        // 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호가 일치한다면
        if(passwordEncoder.matches(request.getPassword(), member.getPassword())) {

            // AccessToken : 1시간 1 * 60 * 60 * 1000
            // RefreshToken : 2주 14 * 24 * 60 * 60 * 1000
            String accessToken = JwtUtil.generateToken(member.getEmail(), secretKey, 1 * 60 * 60 * 1000);
            String refreshToken = JwtUtil.generateToken(member.getEmail(), secretKey, 14 * 24 * 60 * 60 * 1000);

            redisService.setKeyAndValue(refreshToken, member.getId());

            System.out.println("AccessToken: " + accessToken);
            System.out.println("RefreshToken: " + refreshToken);

            // AccessCookie : 1시간 1 * 60 * 60
            // RefreshCookie : 2주 14 * 24 * 60 * 60
            Cookie accessCookie = JwtUtil.generateCookie("AccessToken", accessToken, 1 * 60 * 60, false);
            Cookie refreshCookie = JwtUtil.generateCookie("RefreshToken", refreshToken, 14 * 24 * 60 * 60, true);

            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);

            return true;
        }

        return false;
    }

    // JWT 토큰 검증 후 Role 확인(완료)
    @Override
    public AuthResponse authorize(HttpServletRequest request) {
        String accessToken = JwtUtil.extractTokenByCookie(request, "AccessToken");
        String email = JwtUtil.getEmail(accessToken, secretKey);

        final Member member = findMemberByEmail(email);

        if(member != null) {
            Optional<MemberRole> maybeMemberRole = memberRoleRepository.findByMember(member);
            if(maybeMemberRole.isPresent()) {
                return new AuthResponse(email, maybeMemberRole.get().getRole().getRoleType().toString());
            }
        }
        return null;
    }

    // 일반 회원 프로필 가져오기(완료)
    @Override
    public UserProfileResponse getUserProfile(HttpServletRequest request) {
        String accessToken = JwtUtil.extractTokenByCookie(request, "AccessToken");
        String email = JwtUtil.getEmail(accessToken, secretKey);

        final Member member = findMemberByEmail(email);
        if(member != null) {
            Optional<UserProfile> maybeUserProfile = userProfileRepository.findUserProfileByMember(member);
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
        return null;
    }

    // 판매자 회원 고객센터 정보 가져오기(완료)
    @Override
    public SellerInfoResponse getSellerInfo(HttpServletRequest request) {
        String accessToken = JwtUtil.extractTokenByCookie(request, "AccessToken");
        String email = JwtUtil.getEmail(accessToken, secretKey);

        final Member member = findMemberByEmail(email);
        if(member != null) {
            Optional<SellerInfo> maybeSellerInfo = sellerInfoRepository.findSellerInfoByMember(member);
            Optional<MemberRole> maybeMemberRole = memberRoleRepository.findByMember(member);

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
        return null;
    }

    // 로그아웃(완료)
    @Override
    public Boolean logout(HttpServletRequest request, HttpServletResponse response) {

        // accessToken를 추출하여 추후 redis BlackList에 등록 예정
        String accessToken = JwtUtil.extractTokenByCookie(request, "AccessToken");
        String refreshToken = JwtUtil.extractTokenByCookie(request, "RefreshToken");

        if(accessToken != null && refreshToken != null) {
            Cookie assessCookie = JwtUtil.generateCookie("AccessToken", null, 0, false);
            response.addCookie(assessCookie);

            Cookie refreshCookie = JwtUtil.generateCookie("RefreshToken", null, 0, true);
            response.addCookie(refreshCookie);
            redisService.deleteByKey(refreshToken);

            return true;
        }
        return false;
    }

    // 일반 회원의 회원탙퇴(완료)
    @Override
    public Boolean withdrawal(HttpServletRequest request, HttpServletResponse response, MemberPasswordCheckRequest passwordCheckRequest) {

        String accessToken = JwtUtil.extractTokenByCookie(request, "AccessToken");
        String refreshToken = JwtUtil.extractTokenByCookie(request, "RefreshToken");

        String emailByAccessToken = JwtUtil.getEmail(accessToken, secretKey);
        String emailByRefreshToken = JwtUtil.getEmail(refreshToken, secretKey);

        if(!emailByAccessToken.equals(emailByRefreshToken)) {
            log.info("The email in both tokens does not match");
            return false;
        }

        final Member member = findMemberByEmail(emailByAccessToken);

        if(member == null) {
            return false;
        }

        if (!passwordEncoder.matches(passwordCheckRequest.getPassword(), member.getPassword())) {
            log.info("Wrong password");
            return false;
        }

        List<Reservation> reservationList = reservationRepository.findAllByMember(member);

        // 현재 시점을 기준으로 Reservation 상태를 확인하여 업데이트 후
        // 예약건의 체크인 날짜가 현재 시점보다 이후인지 확인
        for(Reservation reservation: reservationList) {
            ReservationStatus reservationStatus = reservationStatusRepository.findByReservation(reservation);

            if(reservation.getCheckOutDate().equals(
                    LocalDate.now()) || reservation.getCheckOutDate().isBefore(LocalDate.now())){
                reservationStatus.setStatus(Status.COMPLETED);
                reservationStatusRepository.save(reservationStatus);
            }

            if(reservation.getCheckInDate().isAfter(LocalDate.now()) || reservationStatus.getStatus().equals(Status.REQUESTED)){
                log.info("Reservation has not been used");
                return false;
            }
        }

        // 이용 전 예약건이 없다면 DB에서 모두 삭제
        reservationStatusRepository.deleteByReservationIn(reservationList);
        reservationRepository.deleteAllByMember(member);
        userProfileRepository.deleteByMemberId(member.getId());
        memberRoleRepository.deleteByMemberId(member.getId());
        memberRepository.delete(member);

        // 로그아웃
        boolean logoutResult = logout(request, response);
        return logoutResult;
    }
}
