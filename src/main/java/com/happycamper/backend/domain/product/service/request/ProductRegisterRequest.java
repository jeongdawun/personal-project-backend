package com.happycamper.backend.domain.product.service.request;

import com.happycamper.backend.domain.product.entity.FacilityType;
import com.happycamper.backend.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductRegisterRequest {
    final private String productName;
    final private String category;
    final private String productDetails;
    final private String address;
    final private List<FacilityType> facilityType;
    final private String mainImageName;
    final private List<String> imageNameList;

    public Product toProduct() {
        return new Product(productName, category, productDetails, address);
    }
}
