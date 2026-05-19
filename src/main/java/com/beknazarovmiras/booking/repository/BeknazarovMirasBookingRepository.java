package com.beknazarovmiras.booking.repository;

import com.beknazarovmiras.booking.entity.BeknazarovMirasBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BeknazarovMirasBookingRepository extends JpaRepository<BeknazarovMirasBooking, Long> {

    Page<BeknazarovMirasBooking> findByUserId(Long userId, Pageable pageable);

    List<BeknazarovMirasBooking> findByUserId(Long userId);

    @Query("SELECT b FROM BeknazarovMirasBooking b WHERE b.room.id = :roomId AND " +
           "b.status IN ('PENDING','CONFIRMED') AND " +
           "NOT (b.checkOutDate <= :checkIn OR b.checkInDate >= :checkOut)")
    List<BeknazarovMirasBooking> findConflictingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);

    @Query("SELECT b FROM BeknazarovMirasBooking b WHERE " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:userId IS NULL OR b.user.id = :userId)")
    Page<BeknazarovMirasBooking> findByFilters(
            @Param("status") BeknazarovMirasBooking.BookingStatus status,
            @Param("userId") Long userId,
            Pageable pageable);
}
