package com.happycamper.backend.member.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SellerInfoResponse {
    private String email;
    private Long businessNumber;
    private String businessName;
    private String city;
    private String street;
    private String addressDetail;
    private String zipcode;
    private Long contactNumber;
    private String bank;
    private Long accountNumber;

    public SellerInfoResponse(String email, Long businessNumber, String businessName) {
        this.email = email;
        this.businessNumber = businessNumber;
        this.businessName = businessName;
    }

    public SellerInfoResponse(String email, Long businessNumber, String businessName, String city, String street, String addressDetail, String zipcode, Long contactNumber, String bank, Long accountNumber) {
        this.email = email;
        this.businessNumber = businessNumber;
        this.businessName = businessName;
        this.city = city;
        this.street = street;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
        this.contactNumber = contactNumber;
        this.bank = bank;
        this.accountNumber = accountNumber;
    }
}
