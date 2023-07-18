package com.happycamper.backend.reservation.service.response;

import com.happycamper.backend.reservation.entity.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MyReservationResponseForm {
    final private Long productId;
    final private String productName;
    final private String optionName;
    final private Integer payment;
    final private LocalDate checkInDate;
    final private LocalDate checkOutDate;
    final private Status status;
}
