package com.happycamper.backend.product.controller.form;

import com.happycamper.backend.product.service.request.ProductModifyRequest;
import com.happycamper.backend.product.service.request.ProductOptionModifyRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductModifyRequestForm {
    final private String productDetails;
    final private List<String> imageNameList;
    final private List<String> optionNameList;
    final private List<Integer> optionPriceList;
    final private List<ProductOptionModifyRequestForm> optionModifyRequestFormList;

    public ProductModifyRequest toProductModifyRequest() {
        return new ProductModifyRequest(productDetails, imageNameList);
    }

    public ProductOptionModifyRequest toProductOptionModifyRequest() {
        return new ProductOptionModifyRequest(optionNameList, optionPriceList, optionModifyRequestFormList);
    }
}
