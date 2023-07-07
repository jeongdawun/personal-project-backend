package com.happycamper.backend.member.repository;

import com.happycamper.backend.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
    Optional<MemberRole> findByBusinessNumber(Long businessNumber);
}

