package com.happycamper.backend.domain.reservation.entity;

import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.product.entity.Product;
import com.happycamper.backend.domain.product.entity.ProductOption;
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
    private int totalPrice;
    private String bookingNotes;

    @Setter
    private String partner_order_id;    // 가맹점 주문번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @Setter
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    @Setter
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    public Reservation(LocalDate reservationDate, String userName, Long contactNumber, LocalDate checkInDate, LocalDate checkOutDate, int amount, int totalPrice, String bookingNotes, Product product, ProductOption productOption, Member member) {
        this.reservationDate = reservationDate;
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.bookingNotes = bookingNotes;
        this.product = product;
        this.productOption = productOption;
        this.member = member;
    }
}
