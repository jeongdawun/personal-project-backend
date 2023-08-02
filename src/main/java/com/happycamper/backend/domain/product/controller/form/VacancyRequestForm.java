package com.happycamper.backend.domain.product.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VacancyRequestForm {
    final private Long id;
    final private String checkInDate;
    final private String checkOutDate;
}
