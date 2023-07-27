package com.happycamper.backend.domain.reservation.service.response;

import com.happycamper.backend.domain.payment.entity.Amount;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MyReservationDetailResponseForm {
    final private String partner_order_id;
    final private Long productId;
    final private String productName;
    final private String optionName;
    final private Integer payment;
    final private LocalDate checkInDate;
    final private LocalDate checkOutDate;
    final private String payment_method_type; // 결제 수단
    final private Amount amount;              // 결제 금액 정보
    final private String approved_at;         // 결제 승인 시간
}
