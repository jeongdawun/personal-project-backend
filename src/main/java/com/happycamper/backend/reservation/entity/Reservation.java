package com.happycamper.backend.reservation.entity;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.product.entity.ProductOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {
    @Id
    @Getter
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate reservationDate;
    private String userName;
    private Long contactNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int amount;
    private int payment;
    private String bookingNotes;

    @ManyToOne
    @JoinColumn(name = "product_option_id")
    @Setter
    private ProductOption productOption;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    public Reservation(LocalDate reservationDate, String userName, Long contactNumber, LocalDate checkInDate, LocalDate checkOutDate, int amount, int payment, String bookingNotes, ProductOption productOption, Member member) {
        this.reservationDate = reservationDate;
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.amount = amount;
        this.payment = payment;
        this.bookingNotes = bookingNotes;
        this.productOption = productOption;
        this.member = member;
    }
}
