package com.happycamper.backend.member.service.request;

import com.happycamper.backend.member.entity.sellerInfo.Address;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SellerInfoRegisterRequest {
    final private Address address;
    final private Long contactNumber;
    final private String bank;
    final private Long accountNumber;
}
