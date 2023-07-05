package com.happycamper.backend.member.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequestForm {
    private String authorizationHeader;
    public AuthRequestForm(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
    }
}
