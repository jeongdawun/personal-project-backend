package com.happycamper.backend.member.service.response;

import com.happycamper.backend.member.entity.sellerInfo.Address;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SellerInfoResponse {
    final private String email;
    final private String city;
    final private String street;
    final private String addressDetail;
    final private String zipcode;
    final private Long contactNumber;
    final private String bank;
    final private Long accountNumber;
}
