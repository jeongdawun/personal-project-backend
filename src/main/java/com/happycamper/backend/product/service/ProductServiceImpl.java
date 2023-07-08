package com.happycamper.backend.product.service;

import com.happycamper.backend.product.controller.form.CheckProductNameDuplicateRequestForm;
import com.happycamper.backend.product.entity.*;
import com.happycamper.backend.product.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    final private ProductRepository productRepository;

    @Override
    public Boolean checkProductNameDuplicate(CheckProductNameDuplicateRequestForm requestForm) {
        Optional<Product> maybeProduct = productRepository.findByProductName(requestForm.getProductName());

        if(maybeProduct.isPresent()) {
            return true;
        }
        return false;
    }

}
