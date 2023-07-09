package com.happycamper.backend.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_Id")
    private Role role;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_Id")
    private Member member;

    private Long businessNumber;    // 사업자 번호

    private String businessName;    // 상호명

    // 일반 회원용
    public MemberRole(Role role, Member member) {
        this.role = role;
        this.member = member;
    }

    // 판매자 회원용
    public MemberRole(Role role, Member member, Long businessNumber, String businessName) {
        this.role = role;
        this.member = member;
        this.businessNumber = businessNumber;
        this.businessName = businessName;
    }
}
