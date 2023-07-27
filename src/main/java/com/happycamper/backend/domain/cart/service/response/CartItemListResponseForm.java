package com.happycamper.backend.domain.cart.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class CartItemListResponseForm {
    final private Long id;
    final private Long productId;
    final private String productName;
    final private Long optionId;
    final private String optionName;
    final private Integer payment;
    final private LocalDate checkInDate;
    final private LocalDate checkOutDate;
}
