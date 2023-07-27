package com.happycamper.backend.domain.member.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileRegisterRequest {
    final private String email;
    final private String name;
    final private Long contactNumber;
    final private String nickName;
    final private String birthday;
}
