package com.happycamper.backend.domain.product.controller.form;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductOptionModifyRequestForm {

    final private Long id;
    final private String dateList;
    final private Integer campsiteVacancyList;
}
