package com.beknazarovmiras.booking.repository;

import com.beknazarovmiras.booking.entity.BeknazarovMirasUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BeknazarovMirasUserRepository extends JpaRepository<BeknazarovMirasUser, Long> {
    Optional<BeknazarovMirasUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Query("SELECT u FROM BeknazarovMirasUser u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%',:name,'%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:name,'%'))")
    java.util.List<BeknazarovMirasUser> searchByName(String name);
}
