package com.happycamper.backend.cart.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class CompareCartItemListResponseForm {
    final private Long id;
    final private Long productId;
    final private String productName;
    final private String category;
    final private Long optionId;
    final private String optionName;
    final private String address;
    final private Integer payment;
    final private LocalDate checkInDate;
    final private LocalDate checkOutDate;
}
