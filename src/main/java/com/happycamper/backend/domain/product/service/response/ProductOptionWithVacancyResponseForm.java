package com.happycamper.backend.domain.product.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductOptionWithVacancyResponseForm {
    final private Long id;
    final private LocalDate dateList;
    final private Integer campsiteVacancyList;
}
