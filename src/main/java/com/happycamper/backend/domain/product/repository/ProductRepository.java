package com.happycamper.backend.domain.product.repository;

import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.product.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);
    @Query("SELECT p FROM Product p WHERE p.category = :category")
    List<Product> findAllByCategory(@Param("category") String category);

    @Query("SELECT p FROM Product p JOIN FETCH p.member m WHERE p.id = :id")
    Optional<Product> findProductById(@Param("id") Long id);

    @Query("SELECT p FROM Product p JOIN FETCH p.member m WHERE p.member = :member")
    Optional<Product> findByMember(Member member);

    @EntityGraph(attributePaths = "member")
    Optional<Product> findWithMemberById(Long id);

    List<Product> findAllByProductNameContaining(String keyword);

    @Query("SELECT p.id FROM Product p")
    List<Long> findIdAll();
}
