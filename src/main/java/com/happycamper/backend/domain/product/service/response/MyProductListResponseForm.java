package com.happycamper.backend.domain.product.service.response;

import com.happycamper.backend.domain.product.entity.Product;
import com.happycamper.backend.domain.product.entity.ProductImage;
import com.happycamper.backend.domain.product.entity.ProductMainImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyProductListResponseForm {
    final private Long id;
    final private String productName;
    final private String category;
    final private String productDetails;
    final private String productMainImage;
    final private List<String> productImageNameList  = new ArrayList<>();
    final private List<ProductOptionResponseForm> productOptionResponseFormList;
    final private List<ProductOptionWithVacancyResponseForm> productOptionWithVacancyResponseFormList;

    public MyProductListResponseForm(Product product, List<ProductOptionResponseForm> productOptionResponseFormList, List<ProductOptionWithVacancyResponseForm> productOptionWithVacancyResponseFormList, ProductMainImage productMainImage, List<ProductImage> productImagesList) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.productDetails = product.getProductDetails();
        this.productOptionResponseFormList = productOptionResponseFormList;
        this.productOptionWithVacancyResponseFormList = productOptionWithVacancyResponseFormList;
        this.productMainImage = productMainImage.getMainImageName();

        for (ProductImage images: productImagesList) {
            this.productImageNameList.add(images.getImageName());
        }
    }
}
