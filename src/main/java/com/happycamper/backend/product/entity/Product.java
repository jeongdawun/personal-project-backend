package com.happycamper.backend.product.entity;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.sellerInfo.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Integer category;
    private String productDetails;
    @Embedded
    @Setter
    @Column(nullable = true)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    public Product(String productName, Integer category, String productDetails, Address address) {
        this.productName = productName;
        this.category = category;
        this.productDetails = productDetails;
        this.address = address;
    }
}
