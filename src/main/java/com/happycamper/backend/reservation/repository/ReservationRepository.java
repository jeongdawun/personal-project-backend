package com.happycamper.backend.reservation.repository;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.product.entity.Product;
import com.happycamper.backend.product.entity.ProductOption;
import com.happycamper.backend.reservation.entity.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r JOIN FETCH r.member m WHERE r.member = :member")
    List<Reservation> findAllByMember(Member member);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.product p WHERE r.product = :product")
    List<Reservation> findAllByProduct(Product product);

    @Query("SELECT r.product FROM Reservation r JOIN r.product p WHERE r.id = :id")
    Optional<Product> findProductById(Long id);

    @Query("SELECT r.productOption FROM Reservation r JOIN r.productOption po WHERE r.id = :id")
    Optional<ProductOption> findProductOptionById(Long id);

    @Transactional
    void deleteAllByMember(Member member);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.member JOIN FETCH r.product JOIN FETCH r.productOption WHERE r.id = :reservationId")
    Optional<Reservation> findByIdWithMember(Long reservationId);
}
