package com.happycamper.backend.domain.product.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CampsiteVacancyByMapResponseForm {
    final private Long id;
    final private Integer vacancy;
    final private String address;
}
