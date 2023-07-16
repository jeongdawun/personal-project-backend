package com.happycamper.backend.product.repository;

import com.happycamper.backend.product.entity.ProductMainImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductMainImageRepository extends JpaRepository<ProductMainImage, Long> {
    @Transactional
    void deleteByProductId(Long id);

    @Query("select pmi from ProductMainImage pmi join fetch pmi.product p where p.id = :id")
    ProductMainImage findByProductId(Long id);
}
