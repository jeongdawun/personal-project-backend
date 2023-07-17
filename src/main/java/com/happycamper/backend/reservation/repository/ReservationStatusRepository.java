package com.happycamper.backend.reservation.repository;

import com.happycamper.backend.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {
}
