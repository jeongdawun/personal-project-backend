package com.happycamper.backend.member.controller.form;

import com.happycamper.backend.member.entity.sellerInfo.Address;
import com.happycamper.backend.member.service.request.SellerInfoRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SellerInfoRegisterRequestForm {
    final private String email;
    final private String city;
    final private String street;
    final private String addressDetail;
    final private String zipcode;
    final private Long contactNumber;
    final private String bank;
    final private Long accountNumber;

    public Address toAddress() {
        return new Address(city, street, addressDetail, zipcode);
    }
    public SellerInfoRegisterRequest toSellerInfoRegisterRequest() {
        Address address = toAddress();
        return new SellerInfoRegisterRequest(email, address, contactNumber, bank, accountNumber);
    }
}
