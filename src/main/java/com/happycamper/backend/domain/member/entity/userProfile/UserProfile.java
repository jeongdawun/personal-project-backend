package com.happycamper.backend.domain.member.entity.userProfile;

import com.happycamper.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;
    @Setter
    private Long contactNumber;
    @Setter
    private String nickName;
    @Setter
    private String birthday;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_Id")
    private Member member;

    public UserProfile(String name, Long contactNumber, String nickName, String birthday, Member member) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.nickName = nickName;
        this.birthday = birthday;
        this.member = member;
    }
}
