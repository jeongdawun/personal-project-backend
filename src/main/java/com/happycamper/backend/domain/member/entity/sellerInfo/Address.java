package com.happycamper.backend.domain.member.entity.sellerInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Address {
    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String addressDetail;

    @Column(nullable = false)
    private String zipcode;

    public Address(String city, String street, String addressDetail, String zipcode) {
        this.city = city;
        this.street = street;
        this.addressDetail = addressDetail;
        this.zipcode = zipcode;
    }
}
