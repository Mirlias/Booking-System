package com.beknazarovmiras.booking.controller;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasBookingRequest;
import com.beknazarovmiras.booking.dto.response.*;
import com.beknazarovmiras.booking.entity.BeknazarovMirasBooking;
import com.beknazarovmiras.booking.service.BeknazarovMirasBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Bookings", description = "Booking management")
public class BeknazarovMirasBookingController {

    private final BeknazarovMirasBookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasBookingResponse>> create(
            @Valid @RequestBody BeknazarovMirasBookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BeknazarovMirasApiResponse.success("Booking created",
                        bookingService.create(request, userDetails.getUsername())));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my bookings with pagination")
    public ResponseEntity<BeknazarovMirasApiResponse<Page<BeknazarovMirasBookingResponse>>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(
                bookingService.getMyBookings(userDetails.getUsername(), pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasBookingResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(bookingService.getById(id)));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasBookingResponse>> cancel(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success("Booking cancelled",
                bookingService.cancelBooking(id, userDetails.getUsername())));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bookings with filter (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<Page<BeknazarovMirasBookingResponse>>> getAll(
            @RequestParam(required = false) BeknazarovMirasBooking.BookingStatus status,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(
                bookingService.getAllBookings(status, userId, pageable)));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update booking status (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasBookingResponse>> updateStatus(
            @PathVariable Long id, @RequestParam BeknazarovMirasBooking.BookingStatus status) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(bookingService.updateStatus(id, status)));
    }
}
