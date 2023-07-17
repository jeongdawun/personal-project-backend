package com.happycamper.backend.reservation.repository;

import com.happycamper.backend.reservation.entity.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, Long> {
}
