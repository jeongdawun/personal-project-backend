package com.happycamper.backend.member.repository;

import com.happycamper.backend.member.entity.Role;
import com.happycamper.backend.member.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
