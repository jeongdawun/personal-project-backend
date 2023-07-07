package com.happycamper.backend.member.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileResponse {
    final private String email;
    final private String name;
    final private Long contactNumber;
    final private String nickName;
    final private String birthday;
}
