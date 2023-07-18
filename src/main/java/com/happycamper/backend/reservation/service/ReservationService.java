package com.happycamper.backend.reservation.service;

import com.happycamper.backend.reservation.controller.form.ReservationRequestForm;
import com.happycamper.backend.reservation.service.response.MyReservationResponseForm;

import java.util.List;

public interface ReservationService {
    Boolean register(String email, ReservationRequestForm requestForm);
    List<MyReservationResponseForm> searchMyReservation(String email);
}
