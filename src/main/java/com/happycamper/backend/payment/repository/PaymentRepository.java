package com.happycamper.backend.payment.repository;

import com.happycamper.backend.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.partner_order_id = :partner_order_id")
    Optional<Payment> findByPartner_order_id(String partner_order_id);
}
