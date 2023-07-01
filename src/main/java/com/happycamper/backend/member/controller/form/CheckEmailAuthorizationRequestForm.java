package com.happycamper.backend.member.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckEmailAuthorizationRequestForm {
    final private String email;
}
