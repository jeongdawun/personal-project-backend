package com.happycamper.backend.member.service;

public interface RedisService {
    void setKeyAndValue(String token, Long accountId);
    Long getValueByKey(String token);
}
