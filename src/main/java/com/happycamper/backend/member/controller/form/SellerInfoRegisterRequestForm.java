package com.happycamper.backend.member.controller.form;

import com.happycamper.backend.member.entity.sellerInfo.Address;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SellerInfoRegisterRequestForm {
    final private Address address;
    final private Long contactNumber;
    final private String bank;
    final private Long accountNumber;

    public SellerInfoRegisterRequest toSellerInfoRegisterRequest() {
        return new SellerInfoRegisterRequest(address, contactNumber, bank, accountNumber);
    }
}
