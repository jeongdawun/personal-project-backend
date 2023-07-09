package com.happycamper.backend.product.service.request;

import com.happycamper.backend.member.entity.sellerInfo.Address;
import com.happycamper.backend.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductRegisterRequest {
    final private String productName;
    final private Integer category;
    final private String productDetails;
    final private Address address;
    final private String mainImageName;
    final private List<String> imageNameList;

    public Product toProduct() {
        return new Product(productName, category, productDetails, address);
    }
}