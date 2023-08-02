package com.happycamper.backend.domain.product.controller.form;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class OptionsModifyRequestForm {

    final private Long id; // ProductOption의 id
    final private String dateList;
    final private Integer campsiteVacancyList;
}
