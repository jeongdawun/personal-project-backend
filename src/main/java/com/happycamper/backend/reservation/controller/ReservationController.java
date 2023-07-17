package com.happycamper.backend.reservation.controller;

import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.reservation.controller.form.ReservationRequestForm;
import com.happycamper.backend.reservation.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    final private ReservationService reservationService;
    final private MemberService memberService;
    @PostMapping("/create")
    public Boolean reservation(HttpServletRequest request, @RequestBody ReservationRequestForm requestForm) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        // 비즈니스 계정은 예약 불가
        if(authResponse.getRole() == "BUSINESS") {
            return false;
        }
        Boolean isCompleteReservation = reservationService.register(email, requestForm);
        return isCompleteReservation;
    }
}
