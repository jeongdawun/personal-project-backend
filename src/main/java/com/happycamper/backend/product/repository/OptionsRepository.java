package com.happycamper.backend.product.repository;

import com.happycamper.backend.product.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OptionsRepository extends JpaRepository<Options, Long> {
    @Transactional
    void deleteAllByProductOptionId(Long id);
}
