package com.happycamper.backend.product.repository;

import com.happycamper.backend.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Transactional
    void deleteAllByProductId(Long id);
}
