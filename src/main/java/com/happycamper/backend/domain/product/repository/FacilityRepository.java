package com.happycamper.backend.domain.product.repository;

import com.happycamper.backend.domain.product.entity.Facility;
import com.happycamper.backend.domain.product.entity.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Facility> findByFacilityType(FacilityType facilityType);
}
