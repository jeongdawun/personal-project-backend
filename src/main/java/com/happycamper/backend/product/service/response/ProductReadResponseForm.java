package com.happycamper.backend.product.service.response;

import com.happycamper.backend.product.entity.Product;
import com.happycamper.backend.product.entity.ProductImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductReadResponseForm {

    final private Long id;
    final private String productName;
    final private Integer category;
    final private String productDetails;
    final private List<String> productImageNameList  = new ArrayList<>();
    final private List<ProductOptionResponseForm> responseFormList;

        public ProductReadResponseForm(Product product, List<ProductOptionResponseForm> responseFormList, List<ProductImage> productImagesList) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.productDetails = product.getProductDetails();
        this.responseFormList = responseFormList;

        for (ProductImage images: productImagesList) {
            this.productImageNameList.add(images.getImageName());
        }
    }

}
