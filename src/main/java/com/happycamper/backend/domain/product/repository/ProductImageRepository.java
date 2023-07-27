package com.happycamper.backend.domain.product.repository;

import com.happycamper.backend.domain.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Transactional
    void deleteAllByProductId(Long id);
    @Query("select pi from ProductImage pi join fetch pi.product p where p.id = :id")
    List<ProductImage> findAllByProductId(Long id);
}
