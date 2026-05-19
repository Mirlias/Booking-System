package com.beknazarovmiras.booking.controller;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasRoomRequest;
import com.beknazarovmiras.booking.dto.response.*;
import com.beknazarovmiras.booking.entity.BeknazarovMirasRoom;
import com.beknazarovmiras.booking.service.BeknazarovMirasRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "Room management")
public class BeknazarovMirasRoomController {

    private final BeknazarovMirasRoomService roomService;

    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "Get rooms by hotel with pagination")
    public ResponseEntity<BeknazarovMirasApiResponse<Page<BeknazarovMirasRoomResponse>>> getByHotel(
            @PathVariable Long hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "pricePerNight") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(roomService.getByHotel(hotelId, pageable)));
    }

    @GetMapping("/hotel/{hotelId}/filter")
    @Operation(summary = "Filter rooms by type, price, capacity")
    public ResponseEntity<BeknazarovMirasApiResponse<Page<BeknazarovMirasRoomResponse>>> filter(
            @PathVariable Long hotelId,
            @RequestParam(required = false) BeknazarovMirasRoom.RoomType type,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(
                roomService.filterRooms(hotelId, type, minPrice, maxPrice, minCapacity, pageable)));
    }

    @GetMapping("/hotel/{hotelId}/available")
    @Operation(summary = "Get available rooms for date range")
    public ResponseEntity<BeknazarovMirasApiResponse<List<BeknazarovMirasRoomResponse>>> getAvailable(
            @PathVariable Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(
                roomService.getAvailableRooms(hotelId, checkIn, checkOut)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasRoomResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(roomService.getById(id)));
    }

    @PostMapping("/hotel/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create room (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasRoomResponse>> create(
            @PathVariable Long hotelId, @Valid @RequestBody BeknazarovMirasRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BeknazarovMirasApiResponse.success("Room created", roomService.create(hotelId, request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update room (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasRoomResponse>> update(
            @PathVariable Long id, @Valid @RequestBody BeknazarovMirasRoomRequest request) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(roomService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete room (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<Void>> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success("Room deleted", null));
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Upload room image (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasRoomResponse>> uploadImage(
            @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(roomService.uploadImage(id, file)));
    }
}
