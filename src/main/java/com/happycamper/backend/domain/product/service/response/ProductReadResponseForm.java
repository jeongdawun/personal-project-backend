package com.happycamper.backend.domain.product.service.response;

import com.happycamper.backend.domain.product.entity.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductReadResponseForm {

    final private Long id;
    final private String productName;
    final private String category;

    final private String address;
    final private String productDetails;
    final private List<String> productImageNameList  = new ArrayList<>();
    final private List<FacilityType> facilities  = new ArrayList<>();
    final private List<ProductOptionResponseForm> productOptionResponseFormList;

        public ProductReadResponseForm(Product product, List<ProductOptionResponseForm> productOptionResponseFormList, List<ProductImage> productImagesList, List<Facility> facilities) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.address = product.getAddress();
        this.category = product.getCategory();
        this.productDetails = product.getProductDetails();
        this.productOptionResponseFormList = productOptionResponseFormList;

        for (ProductImage images: productImagesList) {
            this.productImageNameList.add(images.getImageName());
        }
        for (Facility facility: facilities) {
            this.facilities.add(facility.getFacilityType());
        }
    }
}
