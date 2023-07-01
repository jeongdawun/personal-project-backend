package com.happycamper.backend.member.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileRegisterRequest {
    final private String name;
    final private Long contactNumber;
    final private String nickName;
    final private String birthday;
}
