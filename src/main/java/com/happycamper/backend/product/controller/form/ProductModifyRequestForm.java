package com.happycamper.backend.product.controller.form;

import com.happycamper.backend.product.entity.Options;
import com.happycamper.backend.product.service.request.ProductModifyRequest;
import com.happycamper.backend.product.service.request.ProductOptionModifyRequest;
import com.happycamper.backend.utility.transform.TransformToDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductModifyRequestForm {
    final private String productDetails;
    final private List<String> imageNameList;
    final private List<String> optionNameList;
    final private List<Integer> optionPriceList;
    final private List<List<ProductOptionModifyRequestForm>> optionModifyRequestFormList;

    public List<List<Options>> toOptionsList () {
        List<List<Options>> optionsListList = new ArrayList<>();

        List<Options> optionsList = new ArrayList<>();
        for(List<ProductOptionModifyRequestForm> optionsRequest: optionModifyRequestFormList) {
            for(ProductOptionModifyRequestForm requestForm: optionsRequest) {
                Options options = new Options(TransformToDate.transformToDate(requestForm.getDateList()), requestForm.getCampsiteVacancyList());
                optionsList.add(options);
            }
            optionsListList.add(optionsList);
        }
        return optionsListList;
    }

    public ProductModifyRequest toProductModifyRequest() {
        return new ProductModifyRequest(productDetails, imageNameList);
    }

    public ProductOptionModifyRequest toProductOptionModifyRequest() {
        List<List<Options>> optionsList = toOptionsList();
        return new ProductOptionModifyRequest(optionNameList, optionPriceList, optionsList);
    }
}
