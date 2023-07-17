package com.happycamper.backend.reservation.controller.form;

import com.happycamper.backend.reservation.entity.ReservationDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationRequestForm {
    final private Long productOptionId;
    final private String userName;
    final private Long contactNumber;
    final private String checkInDate;
    final private String checkOutDate;
    final private int amount;
    final private String bookingNotes;

    public ReservationDetails toReservationDetails() {
        return new ReservationDetails(userName, contactNumber, checkInDate, checkOutDate, amount, bookingNotes);
    }
}
