package com.happycamper.backend.domain.product.service;

import com.happycamper.backend.domain.product.service.request.ProductOptionModifyRequest;
import com.happycamper.backend.domain.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.domain.product.service.request.ProductRegisterRequest;
import com.happycamper.backend.domain.product.controller.form.CampsiteVacancyByMapRequestForm;
import com.happycamper.backend.domain.product.controller.form.VacancyRequestForm;
import com.happycamper.backend.domain.product.service.request.ProductModifyRequest;
import com.happycamper.backend.domain.product.service.response.*;

import java.util.List;

public interface ProductService {

    List<ProductListResponseForm> list();
    List<ProductListResponseForm> topList();
    Boolean checkProductNameDuplicate(String productname);
    Boolean register(String email, ProductRegisterRequest productRegisterRequest, ProductOptionRegisterRequest optionRegisterRequest);
    MyProductListResponseForm myList(String email);
    Boolean modify(String email, Long id, ProductModifyRequest productModifyRequest, ProductOptionModifyRequest optionModifyRequest);
    Boolean delete(String email, Long id);
    ProductReadResponseForm read(Long id);
    VacancyResponseForm checkVacancy(VacancyRequestForm requestForm);
    List<ProductListResponseForm> listByCategory(String category);
    List<CampsiteVacancyByMapResponseForm> checkVacancyByDate(CampsiteVacancyByMapRequestForm requestForm);
    List<ProductListResponseForm> listByKeyword(String keyword);
}