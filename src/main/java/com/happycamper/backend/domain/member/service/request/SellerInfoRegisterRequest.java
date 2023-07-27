package com.happycamper.backend.domain.member.service.request;

import com.happycamper.backend.domain.member.entity.sellerInfo.Address;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SellerInfoRegisterRequest {
    final private String email;
    final private Address address;
    final private Long contactNumber;
    final private String bank;
    final private Long accountNumber;
}
