package com.happycamper.backend.domain.member.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberLoginRequest {
    final private String email;
    final private String password;
}
