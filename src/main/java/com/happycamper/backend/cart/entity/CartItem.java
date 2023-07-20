package com.happycamper.backend.cart.entity;

import com.happycamper.backend.product.entity.Product;
import com.happycamper.backend.product.entity.ProductOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class CartItem {
    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int amount;
    private int payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @Setter
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @Setter
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    @Setter
    private ProductOption productOption;

    public CartItem(LocalDate checkInDate, LocalDate checkOutDate, int amount, int payment, Cart cart, Product product, ProductOption productOption) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.amount = amount;
        this.payment = payment;
        this.cart = cart;
        this.product = product;
        this.productOption = productOption;
    }
}
