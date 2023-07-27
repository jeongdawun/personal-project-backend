package com.happycamper.backend.domain.member.repository;

import com.happycamper.backend.domain.member.entity.Role;
import com.happycamper.backend.domain.member.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
