package com.happycamper.backend.product.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CampsiteVacancyByMapRequestForm {
    final private String checkInDate;
    final private String checkOutDate;
}
