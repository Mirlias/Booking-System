package com.beknazarovmiras.booking.service;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasReviewRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeknazarovMirasReviewService {
    BeknazarovMirasReviewResponse create(BeknazarovMirasReviewRequest request, String userEmail);
    Page<BeknazarovMirasReviewResponse> getByHotel(Long hotelId, Pageable pageable);
    void delete(Long id, String userEmail);
}
