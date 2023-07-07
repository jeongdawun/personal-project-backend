package com.happycamper.backend.member.entity.sellerInfo;

import com.happycamper.backend.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class SellerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @Setter
    @Column(nullable = true)
    private Address address;
    @Setter
    private Long contactNumber;
    @Setter
    private String bank;
    @Setter
    private Long accountNumber;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_Id")
    private Member member;

    public SellerInfo(Address address, Long contactNumber, String bank, Long accountNumber, Member member) {
        this.address = address;
        this.contactNumber = contactNumber;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.member = member;
    }
}
