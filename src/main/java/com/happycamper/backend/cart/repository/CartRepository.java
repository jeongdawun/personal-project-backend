package com.happycamper.backend.cart.repository;

import com.happycamper.backend.cart.entity.Cart;
import com.happycamper.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.member m WHERE c.member = :member")
    Optional<Cart> findByMember(Member member);
}
