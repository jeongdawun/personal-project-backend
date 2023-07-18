package com.happycamper.backend.reservation.repository;

import com.happycamper.backend.reservation.entity.Reservation;
import com.happycamper.backend.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
    @Query("SELECT rs FROM ReservationStatus rs JOIN FETCH rs.reservation r WHERE rs.reservation = :reservation")
    ReservationStatus findByReservation(Reservation reservation);
}
