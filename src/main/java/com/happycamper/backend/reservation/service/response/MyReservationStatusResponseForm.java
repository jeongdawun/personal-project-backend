package com.happycamper.backend.reservation.service.response;

import com.happycamper.backend.member.service.response.AuthResponse;
import com.happycamper.backend.reservation.entity.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MyReservationStatusResponseForm {
    final private AuthResponse authResponse;
    final private List<Status> statusList;
    final private List<Integer> amountList;
}
