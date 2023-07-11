package com.happycamper.backend.product.service;

import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.product.controller.form.StockRequestForm;
import com.happycamper.backend.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.product.service.request.ProductRegisterRequest;
import com.happycamper.backend.product.service.response.ProductListResponseForm;
import com.happycamper.backend.product.service.response.ProductReadResponseForm;
import com.happycamper.backend.product.service.response.StockResponseForm;

import java.util.List;

public interface ProductService {
    Boolean checkProductNameDuplicate(CheckProductNameDuplicateRequestForm requestForm);
    Boolean register(String email, ProductRegisterRequest productRegisterRequest, ProductOptionRegisterRequest optionRegisterRequest);
    void delete(Long id);
    List<ProductListResponseForm> list();
    ProductReadResponseForm read(Long id);
    StockResponseForm checkStock(StockRequestForm requestForm);
    List<ProductListResponseForm> listByCategory(String category);
}
