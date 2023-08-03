package com.happycamper.backend.mock.member;

import com.happycamper.backend.config.DBInitializer;
import com.happycamper.backend.domain.member.controller.form.NormalMemberRegisterForm;
import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.member.repository.MemberRepository;
import com.happycamper.backend.domain.member.repository.MemberRoleRepository;
import com.happycamper.backend.domain.member.repository.RoleRepository;
import com.happycamper.backend.domain.member.repository.sellerInfo.SellerInfoRepository;
import com.happycamper.backend.domain.member.repository.userProfile.UserProfileRepository;
import com.happycamper.backend.domain.member.service.EmailService;
import com.happycamper.backend.domain.member.service.MemberServiceImpl;
import com.happycamper.backend.domain.member.service.RedisService;
import com.happycamper.backend.domain.product.repository.FacilityRepository;
import com.happycamper.backend.domain.reservation.repository.ReservationRepository;
import com.happycamper.backend.domain.reservation.repository.ReservationStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.happycamper.backend.domain.member.entity.RoleType.NORMAL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MemberMockingTest {
    @Mock
    private MemberRepository mockMemberRepository;
    @Mock
    private MemberRoleRepository mockMemberRoleRepository;
    @Mock
    private UserProfileRepository mockUserProfileRepository;
    @Mock
    private SellerInfoRepository mockSellerInfoRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private ReservationRepository mockReservationRepository;
    @Mock
    private ReservationStatusRepository mockReservationStatusRepository;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private RedisService mockRedisService;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private FacilityRepository mockFacilityRepository;

    @InjectMocks
    private DBInitializer dbInitializer;

    @InjectMocks
    private MemberServiceImpl mockMemberService;

    @BeforeEach
    public void setup () throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMemberService =
                new MemberServiceImpl(
                        mockMemberRepository,
                        mockMemberRoleRepository,
                        mockUserProfileRepository,
                        mockSellerInfoRepository,
                        mockRoleRepository,
                        mockReservationRepository,
                        mockReservationStatusRepository,
                        mockEmailService,
                        mockRedisService,
                        mockPasswordEncoder);

        dbInitializer = new DBInitializer(mockRoleRepository, mockFacilityRepository);
    }
    @Test
    @DisplayName("Mocking 테스트를 준비합니다.")
    void test () {
        System.out.println("준비합니다.");
    }

    @Test
    @DisplayName("이메일 중복 확인")
    public void 이메일_중복_확인 () {
        String email = "test@test.com";
        Mockito.when(mockMemberRepository.findByEmail(email)).thenReturn(Optional.of(new Member("test@test.com", "test1234")));
        Boolean isDuplicated = mockMemberService.checkEmailDuplicate(email);

        assertTrue(isDuplicated);
    }
}
