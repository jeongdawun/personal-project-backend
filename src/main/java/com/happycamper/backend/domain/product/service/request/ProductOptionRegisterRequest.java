package com.happycamper.backend.domain.product.service.request;

import com.happycamper.backend.domain.product.entity.Options;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductOptionRegisterRequest {
    final private List<String> optionNameList;
    final private List<Integer> optionPriceList;
    final private List<List<Options>> optionsList;
}
