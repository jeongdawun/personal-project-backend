package com.happycamper.backend.product.controller.form;

import com.happycamper.backend.product.entity.Options;
import com.happycamper.backend.product.service.request.ProductOptionModifyRequest;
import com.happycamper.backend.product.service.request.ProductRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductModifyRequestForm {
    final private String productName;
    final private String category;
    final private String productDetails;
    final private String address;
    final private String mainImageName;
    final private List<String> imageNameList;
    final private List<String> optionNameList;
    final private List<Integer> optionPriceList;
    final private List<ProductOptionModifyRequestForm> optionModifyRequestFormList;

    public List<List<Options>> toOptionsList () {
        List<List<Options>> optionsList = new ArrayList<>();

        for(ProductOptionModifyRequestForm optionsRequest: optionModifyRequestFormList) {

            List<Options> generateOptionsList = optionsRequest.generateOptionsList(optionsRequest.getDateList(), optionsRequest.getCampsiteVacancyList());
            optionsList.add(generateOptionsList);
        }
        return optionsList;
    }

    public ProductRegisterRequest toProductRegisterRequest() {
        return new ProductRegisterRequest(productName, category, productDetails, address, mainImageName, imageNameList);
    }

    public ProductOptionModifyRequest toProductOptionModifyRequest() {
        List<List<Options>> optionsList = toOptionsList();
        return new ProductOptionModifyRequest(optionNameList, optionPriceList, optionsList);
    }
}
