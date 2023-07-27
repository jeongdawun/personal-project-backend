package com.happycamper.backend.domain.reservation.service;

import com.happycamper.backend.domain.payment.service.reponse.KakaoReadyResponse;
import com.happycamper.backend.domain.reservation.service.response.MyReservationResponseForm;
import com.happycamper.backend.domain.reservation.service.response.MyReservationStatusResponseForm;
import com.happycamper.backend.domain.reservation.controller.form.ReservationRequestForm;
import com.happycamper.backend.domain.reservation.service.response.MyReservationDetailResponseForm;

import java.util.List;

public interface ReservationService {
    KakaoReadyResponse register(String email, ReservationRequestForm requestForm);
    List<MyReservationResponseForm> searchMyReservation(String email);
    MyReservationStatusResponseForm searchMyReservationStatus(String email);
    MyReservationDetailResponseForm searchMyReservationDetail(String email, Long reservation_id);
}
