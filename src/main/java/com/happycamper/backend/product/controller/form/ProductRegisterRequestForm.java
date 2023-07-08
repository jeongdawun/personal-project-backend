package com.happycamper.backend.product.controller.form;

import com.happycamper.backend.member.entity.sellerInfo.Address;
import com.happycamper.backend.product.entity.Options;
import com.happycamper.backend.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.product.service.request.ProductRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductRegisterRequestForm {

    final private String accessToken;
    final private String productName;
    final private Integer category;
    final private String productDetails;
    final private String city;
    final private String street;
    final private String addressDetail;
    final private String zipcode;
    final private String mainImageName;
    final private List<String> imageNameList;
    final private List<String> optionNameList;
    final private List<Integer> optionPriceList;
    final private List<List<Options>> optionsList;

    public Address toAddress() {
        return new Address(city, street, addressDetail, zipcode);
    }

    public ProductRegisterRequest toProductRegisterRequest() {
        Address address = toAddress();
        return new ProductRegisterRequest(productName, category, productDetails, address, mainImageName, imageNameList);
    }

    public ProductOptionRegisterRequest toProductOptionRegisterRequest() {
        return new ProductOptionRegisterRequest(optionNameList, optionPriceList, optionsList);
    }

}
