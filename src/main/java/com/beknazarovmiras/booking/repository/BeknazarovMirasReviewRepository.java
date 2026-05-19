package com.beknazarovmiras.booking.repository;

import com.beknazarovmiras.booking.entity.BeknazarovMirasReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BeknazarovMirasReviewRepository extends JpaRepository<BeknazarovMirasReview, Long> {

    Page<BeknazarovMirasReview> findByHotelId(Long hotelId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM BeknazarovMirasReview r WHERE r.hotel.id = :hotelId")
    Double findAverageRatingByHotelId(Long hotelId);

    boolean existsByUserIdAndHotelId(Long userId, Long hotelId);
}
