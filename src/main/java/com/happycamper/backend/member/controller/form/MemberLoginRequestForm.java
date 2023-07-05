package com.happycamper.backend.member.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberLoginRequestForm {
    final private String email;
    final private String password;
}
