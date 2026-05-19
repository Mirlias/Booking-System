package com.beknazarovmiras.booking.repository;

import com.beknazarovmiras.booking.entity.BeknazarovMirasRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BeknazarovMirasRoomRepository extends JpaRepository<BeknazarovMirasRoom, Long> {

    List<BeknazarovMirasRoom> findByHotelId(Long hotelId);

    Page<BeknazarovMirasRoom> findByHotelId(Long hotelId, Pageable pageable);

    @Query("SELECT r FROM BeknazarovMirasRoom r WHERE r.hotel.id = :hotelId AND r.available = true AND " +
           "r.id NOT IN (SELECT b.room.id FROM BeknazarovMirasBooking b WHERE " +
           "b.status IN ('PENDING','CONFIRMED') AND " +
           "NOT (b.checkOutDate <= :checkIn OR b.checkInDate >= :checkOut))")
    List<BeknazarovMirasRoom> findAvailableRooms(
            @Param("hotelId") Long hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);

    @Query("SELECT r FROM BeknazarovMirasRoom r WHERE r.hotel.id = :hotelId AND " +
           "(:type IS NULL OR r.roomType = :type) AND " +
           "(:minPrice IS NULL OR r.pricePerNight >= :minPrice) AND " +
           "(:maxPrice IS NULL OR r.pricePerNight <= :maxPrice) AND " +
           "(:minCapacity IS NULL OR r.capacity >= :minCapacity)")
    Page<BeknazarovMirasRoom> filterRooms(
            @Param("hotelId") Long hotelId,
            @Param("type") BeknazarovMirasRoom.RoomType type,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minCapacity") Integer minCapacity,
            Pageable pageable);
}
