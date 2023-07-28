package com.happycamper.backend.domain.product.service;

import com.happycamper.backend.domain.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.domain.product.service.request.ProductOptionModifyRequest;
import com.happycamper.backend.domain.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.domain.product.service.request.ProductRegisterRequest;
import com.happycamper.backend.domain.product.controller.form.CampsiteVacancyByMapRequestForm;
import com.happycamper.backend.domain.product.controller.form.StockRequestForm;
import com.happycamper.backend.domain.product.service.request.ProductModifyRequest;
import com.happycamper.backend.domain.product.service.response.*;

import java.util.List;

public interface ProductService {
    Boolean checkProductNameDuplicate(CheckProductNameDuplicateRequestForm requestForm);
    Boolean register(String email, ProductRegisterRequest productRegisterRequest, ProductOptionRegisterRequest optionRegisterRequest);
    void delete(String email, Long id);
    List<ProductListResponseForm> list();
    ProductReadResponseForm read(Long id);
    StockResponseForm checkStock(StockRequestForm requestForm);
    List<ProductListResponseForm> listByCategory(String category);
    List<CampsiteVacancyByMapResponseForm> checkVacancyByDate(CampsiteVacancyByMapRequestForm requestForm);
    MyProductListResponseForm myList(String email);
    Boolean modify(String email, Long id, ProductModifyRequest productModifyRequest, ProductOptionModifyRequest optionModifyRequest);
    List<ProductListResponseForm> listByKeyword(String keyword);
    List<ProductListResponseForm> topList();
}