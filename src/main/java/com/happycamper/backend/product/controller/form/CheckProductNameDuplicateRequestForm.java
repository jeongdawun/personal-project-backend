package com.happycamper.backend.product.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckProductNameDuplicateRequestForm {
    private String productName;

    public CheckProductNameDuplicateRequestForm(String productName) {
        this.productName = productName;
    }
}
