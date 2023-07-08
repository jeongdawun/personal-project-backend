package com.happycamper.backend.product.repository;

import com.happycamper.backend.product.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    @Transactional
    void deleteAllByProductId(Long id);
    @Query("select po from ProductOption po join fetch po.product p where p.id = :id")
    List<ProductOption> findAllByProductId(Long id);
}
