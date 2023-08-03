package com.happycamper.backend.mock.member;

import com.happycamper.backend.domain.member.repository.MemberRepository;
import com.happycamper.backend.domain.member.service.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberMockingTest {
    @Mock
    private MemberRepository mockMemberRepository;

    @InjectMocks
    private MemberServiceImpl mockMemberService;

    @BeforeEach
    public void setup () throws Exception {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("Mocking 테스트를 준비합니다.")
    void test () {
        System.out.println("준비합니다.");
    }
}
