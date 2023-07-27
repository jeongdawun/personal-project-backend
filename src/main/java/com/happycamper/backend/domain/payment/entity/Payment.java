package com.happycamper.backend.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @Getter
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tid;                 // 결제 고유 번호
    private String cid;                 // 가맹점 코드
    private String partner_order_id;    // 가맹점 주문번호
    private String partner_user_id;     // 가맹점 회원 id
    private String payment_method_type; // 결제 수단

    @Embedded
    private Amount amount;              // 결제 금액 정보
    private String item_name;           // 상품 이름
    private int quantity;               // 상품 수량
    private String approved_at;         // 결제 승인 시간

    public Payment(String tid, String cid, String partner_order_id, String partner_user_id, String payment_method_type, Amount amount, String item_name, int quantity, String approved_at) {
        this.tid = tid;
        this.cid = cid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.payment_method_type = payment_method_type;
        this.amount = amount;
        this.item_name = item_name;
        this.quantity = quantity;
        this.approved_at = approved_at;
    }
}
