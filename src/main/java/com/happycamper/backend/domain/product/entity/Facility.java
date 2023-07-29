package com.happycamper.backend.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "facility")
@NoArgsConstructor
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FacilityType facilityType;

    public Facility(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public FacilityType getFacilityType () {
        return facilityType;
    }
}
