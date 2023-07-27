package com.happycamper.backend.domain.reservation.service.response;

import com.happycamper.backend.domain.member.service.response.AuthResponse;
import com.happycamper.backend.domain.reservation.entity.Status;
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
