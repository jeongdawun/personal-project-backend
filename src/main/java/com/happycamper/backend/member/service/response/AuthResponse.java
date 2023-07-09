package com.happycamper.backend.member.service.response;

import com.happycamper.backend.member.entity.Role;
import com.happycamper.backend.member.entity.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthResponse {
    final private String email;
    final private RoleType roleType;
}
