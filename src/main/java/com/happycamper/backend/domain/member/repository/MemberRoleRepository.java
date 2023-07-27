package com.happycamper.backend.domain.member.repository;

import com.happycamper.backend.domain.member.entity.Member;
import com.happycamper.backend.domain.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
    Optional<MemberRole> findByBusinessNumber(Long businessNumber);

    @Query("select mr FROM MemberRole mr join fetch mr.member m where m = :member")
    Optional<MemberRole> findByMember(Member member);

    @Transactional
    void deleteByMemberId(Long id);
}

