package com.happycamper.backend.product.service;

import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.product.service.request.ProductRegisterRequest;
import com.happycamper.backend.product.service.response.ProductListResponseForm;

import java.util.List;

public interface ProductService {
    Boolean checkProductNameDuplicate(CheckProductNameDuplicateRequestForm requestForm);
    Boolean register(String email, ProductRegisterRequest productRegisterRequest, ProductOptionRegisterRequest optionRegisterRequest);
    void delete(Long id);
    List<ProductListResponseForm> list();
}
