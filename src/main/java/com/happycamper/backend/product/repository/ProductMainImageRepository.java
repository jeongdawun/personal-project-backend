package com.happycamper.backend.product.repository;

import com.happycamper.backend.product.entity.ProductMainImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProductMainImageRepository extends JpaRepository<ProductMainImage, Long> {
    @Transactional
    void deleteByProductId(Long id);
}
