package com.happycamper.backend.product.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductOptionResponseForm {

    final private Long id;
    final private List<Date> dateList;
    final private List<Integer> stockList;
    final private String optionName;
    final private Integer optionPrice;
}
