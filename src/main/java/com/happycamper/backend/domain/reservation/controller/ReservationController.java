package com.happycamper.backend.domain.reservation.controller;

import com.happycamper.backend.domain.payment.service.reponse.KakaoApproveResponse;
import com.happycamper.backend.domain.payment.service.reponse.KakaoReadyResponse;
import com.happycamper.backend.domain.reservation.service.response.MyReservationResponseForm;
import com.happycamper.backend.domain.reservation.service.response.MyReservationStatusResponseForm;
import com.happycamper.backend.domain.member.service.MemberService;
import com.happycamper.backend.domain.member.service.response.AuthResponse;
import com.happycamper.backend.domain.payment.service.PaymentService;
import com.happycamper.backend.domain.reservation.controller.form.ReservationRequestForm;
import com.happycamper.backend.domain.reservation.service.ReservationService;
import com.happycamper.backend.domain.reservation.service.response.MyReservationDetailResponseForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    final private ReservationService reservationService;
    final private MemberService memberService;
    final private PaymentService paymentService;

    @PostMapping("/create")
    public KakaoReadyResponse reservation(HttpServletRequest request, @RequestBody ReservationRequestForm requestForm) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();

        log.info("create");

        // 비즈니스 계정은 예약 불가
        if(authResponse.getRole() == "BUSINESS") {
            return null;
        }

        return reservationService.register(email, requestForm);
    }

    @GetMapping("/success/{orderId}/{userId}")
    public ResponseEntity afterPayRequest(
            @PathVariable("orderId") String partner_order_id,
            @PathVariable("userId") String partner_user_id,
            @RequestParam("pg_token") String pgToken) {

        log.info("success");

        KakaoApproveResponse kakaoApprove = paymentService.approveResponse(pgToken, partner_order_id, partner_user_id);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public void cancel() {

        throw new RuntimeException("PAY_CANCEL");
    }

    @GetMapping("/fail")
    public void fail() {

        throw new RuntimeException("PAY_FAILED");
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

    @GetMapping("/my-reservation-detail/{reservation_id}")
    public MyReservationDetailResponseForm getMyReservationDetail(HttpServletRequest request, @PathVariable("reservation_id") Long reservation_id) {
        AuthResponse authResponse = memberService.authorize(request);
        String email = authResponse.getEmail();
        return reservationService.searchMyReservationDetail(email, reservation_id);
    }
}
