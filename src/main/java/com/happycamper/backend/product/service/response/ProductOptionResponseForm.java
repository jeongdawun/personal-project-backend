package com.happycamper.backend.product.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductOptionResponseForm {

    final private Long id;
    final private String optionName;
    final private Integer optionPrice;
}
