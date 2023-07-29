package com.happycamper.backend.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class ProductFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "facility_Id")
    private Facility facility;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_Id")
    private Product product;

    public ProductFacility(Facility facility, Product product) {
        this.facility = facility;
        this.product = product;
    }
}
