package com.happycamper.backend.product.service;

import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;

public interface ProductService {
    Boolean checkProductNameDuplicate(CheckProductNameDuplicateRequestForm requestForm);
}
