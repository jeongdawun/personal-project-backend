package com.happycamper.backend.domain.product.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class StockResponseForm {
    final private List<String> optionNameList;
    final private List<Integer> campsiteVacancyList;

}
