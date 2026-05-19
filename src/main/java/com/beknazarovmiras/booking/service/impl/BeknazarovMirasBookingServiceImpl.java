package com.beknazarovmiras.booking.service.impl;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasBookingRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasBookingResponse;
import com.beknazarovmiras.booking.entity.*;
import com.beknazarovmiras.booking.exception.*;
import com.beknazarovmiras.booking.mapper.BeknazarovMirasBookingMapper;
import com.beknazarovmiras.booking.repository.*;
import com.beknazarovmiras.booking.service.BeknazarovMirasBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeknazarovMirasBookingServiceImpl implements BeknazarovMirasBookingService {

    private final BeknazarovMirasBookingRepository bookingRepository;
    private final BeknazarovMirasRoomRepository roomRepository;
    private final BeknazarovMirasUserRepository userRepository;
    private final BeknazarovMirasBookingMapper mapper;

    @Override
    @Transactional
    public BeknazarovMirasBookingResponse create(BeknazarovMirasBookingRequest request, String userEmail) {
        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new BeknazarovMirasBadRequestException("Check-out must be after check-in");
        }
        BeknazarovMirasRoom room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("Room not found: " + request.getRoomId()));
        if (!room.getAvailable()) throw new BeknazarovMirasBadRequestException("Room is not available");

        boolean conflict = !bookingRepository.findConflictingBookings(
                room.getId(), request.getCheckInDate(), request.getCheckOutDate()).isEmpty();
        if (conflict) throw new BeknazarovMirasBadRequestException("Room is already booked for these dates");

        BeknazarovMirasUser user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("User not found"));

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        BigDecimal total = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        BeknazarovMirasBooking booking = BeknazarovMirasBooking.builder()
                .checkInDate(request.getCheckInDate()).checkOutDate(request.getCheckOutDate())
                .totalPrice(total).status(BeknazarovMirasBooking.BookingStatus.PENDING)
                .specialRequests(request.getSpecialRequests()).user(user).room(room).build();

        bookingRepository.save(booking);
        sendConfirmationAsync(userEmail, booking.getId());
        log.info("Booking created: {} for user: {}", booking.getId(), userEmail);
        return mapper.toBookingResponse(booking);
    }

    @Override
    public BeknazarovMirasBookingResponse getById(Long id) {
        return mapper.toBookingResponse(findById(id));
    }

    @Override
    public Page<BeknazarovMirasBookingResponse> getMyBookings(String userEmail, Pageable pageable) {
        BeknazarovMirasUser user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("User not found"));
        return bookingRepository.findByUserId(user.getId(), pageable).map(mapper::toBookingResponse);
    }

    @Override
    public Page<BeknazarovMirasBookingResponse> getAllBookings(
            BeknazarovMirasBooking.BookingStatus status, Long userId, Pageable pageable) {
        return bookingRepository.findByFilters(status, userId, pageable).map(mapper::toBookingResponse);
    }

    @Override
    @Transactional
    public BeknazarovMirasBookingResponse cancelBooking(Long id, String userEmail) {
        BeknazarovMirasBooking booking = findById(id);
        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new BeknazarovMirasUnauthorizedException("You can only cancel your own bookings");
        }
        if (booking.getStatus() == BeknazarovMirasBooking.BookingStatus.CANCELLED) {
            throw new BeknazarovMirasBadRequestException("Booking is already cancelled");
        }
        booking.setStatus(BeknazarovMirasBooking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        log.info("Booking cancelled: {}", id);
        return mapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public BeknazarovMirasBookingResponse updateStatus(Long id, BeknazarovMirasBooking.BookingStatus status) {
        BeknazarovMirasBooking booking = findById(id);
        booking.setStatus(status);
        return mapper.toBookingResponse(bookingRepository.save(booking));
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> sendConfirmationAsync(String email, Long bookingId) {
        log.info("Async: Sending booking confirmation email to {} for booking #{}", email, bookingId);
        // Email sending logic would go here
        return CompletableFuture.completedFuture(null);
    }

    private BeknazarovMirasBooking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("Booking not found: " + id));
    }
}
