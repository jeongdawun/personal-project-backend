package com.happycamper.backend.domain.member.repository.sellerInfo;

import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.member.entity.sellerInfo.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, Long> {
    @Query("select si FROM SellerInfo si join fetch si.member m where m = :member")
    Optional<SellerInfo> findSellerInfoByMember(Member member);
}
