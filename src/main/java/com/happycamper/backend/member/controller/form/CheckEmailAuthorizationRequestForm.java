package com.happycamper.backend.member.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckEmailAuthorizationRequestForm {
    private String email;

    public CheckEmailAuthorizationRequestForm(String email) {
        this.email = email;
    }
}
