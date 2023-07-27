package com.happycamper.backend.domain.product.service.response;

import com.happycamper.backend.domain.product.entity.Product;
import com.happycamper.backend.domain.product.entity.ProductImage;
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
    final private String productDetails;
    final private List<String> productImageNameList  = new ArrayList<>();
    final private List<ProductOptionResponseForm> productOptionResponseFormList;

        public ProductReadResponseForm(Product product, List<ProductOptionResponseForm> productOptionResponseFormList, List<ProductImage> productImagesList) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.productDetails = product.getProductDetails();
        this.productOptionResponseFormList = productOptionResponseFormList;

        for (ProductImage images: productImagesList) {
            this.productImageNameList.add(images.getImageName());
        }
    }

}
