package com.beknazarovmiras.booking.repository;

import com.beknazarovmiras.booking.entity.BeknazarovMirasHotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BeknazarovMirasHotelRepository extends JpaRepository<BeknazarovMirasHotel, Long> {

    Page<BeknazarovMirasHotel> findByCity(String city, Pageable pageable);

    @Query("SELECT h FROM BeknazarovMirasHotel h WHERE " +
           "(:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%',:name,'%'))) AND " +
           "(:city IS NULL OR LOWER(h.city) LIKE LOWER(CONCAT('%',:city,'%'))) AND " +
           "(:minStars IS NULL OR h.starRating >= :minStars) AND " +
           "(:maxStars IS NULL OR h.starRating <= :maxStars)")
    Page<BeknazarovMirasHotel> searchHotels(
            @Param("name") String name,
            @Param("city") String city,
            @Param("minStars") Integer minStars,
            @Param("maxStars") Integer maxStars,
            Pageable pageable);
}
