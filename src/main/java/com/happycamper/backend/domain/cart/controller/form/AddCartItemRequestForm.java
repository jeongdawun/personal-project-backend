package com.happycamper.backend.domain.cart.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddCartItemRequestForm {
    final private Long productId;
    final private Long productOptionId;
    final private String checkInDate;
    final private String checkOutDate;
    final private int amount;
}
