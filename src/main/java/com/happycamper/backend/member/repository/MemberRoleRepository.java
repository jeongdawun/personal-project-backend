package com.happycamper.backend.member.repository;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
    Optional<MemberRole> findByBusinessNumber(Long businessNumber);

    @Query("select mr FROM MemberRole mr join fetch mr.member m where m = :member")
    Optional<MemberRole> findByMember(Member member);
}

