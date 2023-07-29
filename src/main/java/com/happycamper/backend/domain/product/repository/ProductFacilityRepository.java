package com.happycamper.backend.domain.product.repository;

import com.happycamper.backend.domain.product.entity.ProductFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductFacilityRepository extends JpaRepository<ProductFacility, Long> {
    @Transactional
    void deleteAllByProductId(Long id);

    @Query("select pf from ProductFacility pf join fetch pf.product p where p.id = :id")
    List<ProductFacility> findAllByProductId(Long id);
}
