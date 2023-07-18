package com.happycamper.backend.reservation.controller;

import com.happycamper.backend.member.service.MemberService;
import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.reservation.controller.form.ReservationRequestForm;
import com.happycamper.backend.reservation.service.ReservationService;
import com.happycamper.backend.reservation.service.response.MyReservationResponseForm;
import com.happycamper.backend.reservation.service.response.MyReservationStatusResponseForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/my-reservations")
    public List<MyReservationResponseForm> getMyReservations(HttpServletRequest request) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();
        return reservationService.searchMyReservation(email);
    }

    @GetMapping("/my-reservations-status")
    public MyReservationStatusResponseForm getMyReservationStatus(HttpServletRequest request) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();
        return reservationService.searchMyReservationStatus(email);
    }
}
