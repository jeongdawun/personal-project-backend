package com.happycamper.backend.member.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckBusinessNumberDuplicateRequestForm {
    private Long businessNumber;

    public CheckBusinessNumberDuplicateRequestForm(Long businessNumber) {
        this.businessNumber = businessNumber;
    }
}
