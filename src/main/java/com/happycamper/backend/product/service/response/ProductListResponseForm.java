package com.happycamper.backend.product.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductListResponseForm {
    final private String productName;
    final private String category;
    final private String mainImageName;
    final private Integer minOptionPrice;
}
