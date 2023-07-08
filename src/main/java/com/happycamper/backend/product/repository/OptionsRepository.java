package com.happycamper.backend.product.repository;

import com.happycamper.backend.product.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepository extends JpaRepository<Options, Long> {
}
