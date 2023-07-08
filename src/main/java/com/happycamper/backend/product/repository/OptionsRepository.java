package com.happycamper.backend.product.repository;

import com.happycamper.backend.product.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OptionsRepository extends JpaRepository<Options, Long> {
    @Transactional
    void deleteAllByProductOptionId(Long id);
    @Query("select o from Options o join fetch o.productOption po where po.id = :id")
    List<Options> findAllByProductOptionId(Long id);
}
