package com.happycamper.backend.product.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StockRequestForm {
    final private Long id;
    final private String checkInDate;
    final private String checkOutDate;
}
