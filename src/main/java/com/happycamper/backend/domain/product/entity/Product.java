package com.happycamper.backend.domain.product.entity;

import com.happycamper.backend.domain.member.entity.Member;
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
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String category;
    private String productDetails;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    public Product(String productName, String category, String productDetails, String address) {
        this.productName = productName;
        this.category = category;
        this.productDetails = productDetails;
        this.address = address;
    }
}
