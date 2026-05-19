package com.beknazarovmiras.booking.service;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasBookingRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasBookingResponse;
import com.beknazarovmiras.booking.entity.BeknazarovMirasBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeknazarovMirasBookingService {
    BeknazarovMirasBookingResponse create(BeknazarovMirasBookingRequest request, String userEmail);
    BeknazarovMirasBookingResponse getById(Long id);
    Page<BeknazarovMirasBookingResponse> getMyBookings(String userEmail, Pageable pageable);
    Page<BeknazarovMirasBookingResponse> getAllBookings(BeknazarovMirasBooking.BookingStatus status, Long userId, Pageable pageable);
    BeknazarovMirasBookingResponse cancelBooking(Long id, String userEmail);
    BeknazarovMirasBookingResponse updateStatus(Long id, BeknazarovMirasBooking.BookingStatus status);
}
