package com.happycamper.backend.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="product_option")
public class ProductOption {

    @Id
    @Column(name = "product_option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;

    private Integer optionPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductOption(String optionName, Integer optionPrice) {
        this.optionName = optionName;
        this.optionPrice = optionPrice;
    }
}
