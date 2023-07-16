package com.happycamper.backend.product.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductOptionWithVacancyResponseForm {
    final private Long id;
    final private List<LocalDate> dateList;
    final private List<Integer> campsiteVacancyList;
}
