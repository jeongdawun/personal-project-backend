package com.happycamper.backend.member.service;

import io.jsonwebtoken.Claims;

public interface JwtTokenService {
    String generateAccessToken();
    String generateRefreshToken();
    Claims parseJwtToken(String token);
}
