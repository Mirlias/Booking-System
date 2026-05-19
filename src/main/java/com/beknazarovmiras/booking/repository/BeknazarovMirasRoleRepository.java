package com.beknazarovmiras.booking.repository;

import com.beknazarovmiras.booking.entity.BeknazarovMirasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BeknazarovMirasRoleRepository extends JpaRepository<BeknazarovMirasRole, Long> {
    Optional<BeknazarovMirasRole> findByName(BeknazarovMirasRole.RoleName name);
}
