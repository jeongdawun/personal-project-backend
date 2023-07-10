package com.happycamper.backend.member.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthResponse {
    final private String email;
    final private String role;
}
