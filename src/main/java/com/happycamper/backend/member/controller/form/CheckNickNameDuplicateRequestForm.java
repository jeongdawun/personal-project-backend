package com.happycamper.backend.member.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckNickNameDuplicateRequestForm {
    private String nickName;

    public CheckNickNameDuplicateRequestForm(String nickName) {
        this.nickName = nickName;
    }
}
