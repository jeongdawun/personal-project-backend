package com.happycamper.backend.domain.reservation.repository;

import com.happycamper.backend.domain.reservation.entity.ReservationStatus;
import com.happycamper.backend.domain.reservation.entity.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
    @Query("SELECT rs FROM ReservationStatus rs JOIN FETCH rs.reservation r WHERE rs.reservation = :reservation")
    ReservationStatus findByReservation(Reservation reservation);

    @Transactional
    void deleteByReservationIn(List<Reservation> reservationList);
}
