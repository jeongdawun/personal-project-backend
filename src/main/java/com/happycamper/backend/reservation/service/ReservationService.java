package com.happycamper.backend.reservation.service;

import com.happycamper.backend.reservation.controller.form.ReservationRequestForm;

public interface ReservationService {
    Boolean register(String email, ReservationRequestForm requestForm);
}
