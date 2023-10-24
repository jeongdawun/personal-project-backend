package com.happycamper.backend.domain.member.controller.form;

import com.happycamper.backend.domain.member.service.request.UserProfileRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileRegisterRequestForm {
    final private String name;
    final private Long contactNumber;
    final private String nickName;
    final private String birthday;

    public UserProfileRegisterRequest toUserProfileRegisterRequest() {
        return new UserProfileRegisterRequest(name, contactNumber, nickName, birthday);
    }
}
