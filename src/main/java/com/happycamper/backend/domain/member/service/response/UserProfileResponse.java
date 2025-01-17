package com.happycamper.backend.domain.member.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private String email;
    private String name;
    private Long contactNumber;
    private String nickName;
    private String birthday;

    public UserProfileResponse(String email) {
        this.email = email;
    }
}
