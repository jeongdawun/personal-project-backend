package com.happycamper.backend.domain.product.service.request;

import com.happycamper.backend.domain.product.controller.form.ProductOptionModifyRequestForm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductOptionModifyRequest {
    final private List<String> optionNameList;
    final private List<Integer> optionPriceList;
    final private List<ProductOptionModifyRequestForm> optionsList;
}
