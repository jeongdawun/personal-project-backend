package com.happycamper.backend.reservation.service;

import com.happycamper.backend.payment.service.reponse.KakaoReadyResponse;
import com.happycamper.backend.reservation.controller.form.ReservationRequestForm;
import com.happycamper.backend.reservation.service.response.MyReservationDetailResponseForm;
import com.happycamper.backend.reservation.service.response.MyReservationResponseForm;
import com.happycamper.backend.reservation.service.response.MyReservationStatusResponseForm;

import java.util.List;

public interface ReservationService {
    KakaoReadyResponse register(String email, ReservationRequestForm requestForm);
    List<MyReservationResponseForm> searchMyReservation(String email);
    MyReservationStatusResponseForm searchMyReservationStatus(String email);
    MyReservationDetailResponseForm searchMyReservationDetail(String email, Long reservation_id);
}
