package com.happycamper.backend.product.controller.form;

import com.happycamper.backend.product.entity.Options;
import com.happycamper.backend.product.service.request.ProductOptionRegisterRequest;
import com.happycamper.backend.product.service.request.ProductRegisterRequest;
import com.happycamper.backend.utility.transform.TransformToDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductRegisterRequestForm {

    final private String productName;
    final private String category;
    final private String productDetails;
    final private String address;
    final private String mainImageName;
    final private List<String> imageNameList;
    final private List<String> optionNameList;
    final private List<Integer> optionPriceList;
    final private List<ProductOptionRegisterRequestForm> optionsRegisterRequestFormList;

    public List<List<Options>> toOptionsList () {
        List<List<Options>> optionsList = new ArrayList<>();

        for(ProductOptionRegisterRequestForm optionsRequest: optionsRegisterRequestFormList) {
            LocalDate startDate = TransformToDate.transformToDate(optionsRequest.getStartDate());
            LocalDate endDate = TransformToDate.transformToDate(optionsRequest.getEndDate());

            List<Options> generateOptionsList = optionsRequest.generateOptionsList(startDate, endDate, optionsRequest.getCampsiteVacancy());
            optionsList.add(generateOptionsList);
        }
        return optionsList;
    }

    public ProductRegisterRequest toProductRegisterRequest() {
        return new ProductRegisterRequest(productName, category, productDetails, address, mainImageName, imageNameList);
    }

    public ProductOptionRegisterRequest toProductOptionRegisterRequest() {
        List<List<Options>> optionsList = toOptionsList();
        return new ProductOptionRegisterRequest(optionNameList, optionPriceList, optionsList);
    }

}
