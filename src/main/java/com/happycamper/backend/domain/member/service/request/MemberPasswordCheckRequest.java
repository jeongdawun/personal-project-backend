package com.happycamper.backend.domain.member.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberPasswordCheckRequest {
    final private String password;
}
