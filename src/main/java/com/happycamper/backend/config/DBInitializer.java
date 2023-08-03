package com.happycamper.backend.config;

import com.happycamper.backend.domain.member.entity.Role;
import com.happycamper.backend.domain.member.entity.RoleType;
import com.happycamper.backend.domain.member.repository.RoleRepository;
import com.happycamper.backend.domain.product.entity.Facility;
import com.happycamper.backend.domain.product.entity.FacilityType;
import com.happycamper.backend.domain.product.repository.FacilityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBInitializer {

    private final RoleRepository roleRepository;
    private final FacilityRepository facilityRepository;

    @PostConstruct
    public void init () {
        log.debug("initializer 시작!");

        initAccountRoleTypes();
        initFacilityTypes();

        log.debug("initializer 종료!");
    }

    private void initAccountRoleTypes() {
        try {
            final Set<RoleType> roles =
                    roleRepository.findAll().stream()
                            .map(Role::getRoleType)
                            .collect(Collectors.toSet());

            for (RoleType type: RoleType.values()) {
                if (!roles.contains(type)) {
                    final Role role = new Role(type);
                    roleRepository.save(role);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void initFacilityTypes() {
        try {
            final Set<FacilityType> facilityTypes =
                    facilityRepository.findAll().stream()
                            .map(Facility::getFacilityType)
                            .collect(Collectors.toSet());

            for (FacilityType type: FacilityType.values()) {
                if (!facilityTypes.contains(type)) {
                    final Facility facility = new Facility(type);
                    facilityRepository.save(facility);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
