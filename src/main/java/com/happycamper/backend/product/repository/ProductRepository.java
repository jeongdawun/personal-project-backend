package com.happycamper.backend.product.repository;

import com.happycamper.backend.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);
    @Query("SELECT p FROM Product p WHERE p.category = :category")
    List<Product> findAllByCategory(@Param("category") String category);
}
