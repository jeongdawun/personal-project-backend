package com.happycamper.backend.member.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckEmailDuplicateRequestForm {
    private String email;

    public CheckEmailDuplicateRequestForm(String email) {
        this.email = email;
    }
}
