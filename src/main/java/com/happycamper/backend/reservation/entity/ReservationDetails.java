package com.happycamper.backend.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class ReservationDetails {
    @Id
    @Getter
    @Column(name = "reservation_details_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private Long contactNumber;
    private String checkInDate;
    private String checkOutDate;
    private int amount;
    private String bookingNotes;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public ReservationDetails(String userName, Long contactNumber, String checkInDate, String checkOutDate, int amount, String bookingNotes) {
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.amount = amount;
        this.bookingNotes = bookingNotes;
    }
}
