package com.beknazarovmiras.booking.controller;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasHotelRequest;
import com.beknazarovmiras.booking.dto.response.*;
import com.beknazarovmiras.booking.service.BeknazarovMirasHotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Hotel management")
public class BeknazarovMirasHotelController {

    private final BeknazarovMirasHotelService hotelService;

    @GetMapping
    @Operation(summary = "Get all hotels with pagination and sorting")
    public ResponseEntity<BeknazarovMirasApiResponse<Page<BeknazarovMirasHotelResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(hotelService.getAll(pageable)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels with filters")
    public ResponseEntity<BeknazarovMirasApiResponse<Page<BeknazarovMirasHotelResponse>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(required = false) Integer maxStars,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(
                hotelService.search(name, city, minStars, maxStars, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get hotel by ID")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasHotelResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(hotelService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create hotel (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasHotelResponse>> create(
            @Valid @RequestBody BeknazarovMirasHotelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BeknazarovMirasApiResponse.success("Hotel created", hotelService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update hotel (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasHotelResponse>> update(
            @PathVariable Long id, @Valid @RequestBody BeknazarovMirasHotelRequest request) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(hotelService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete hotel (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<Void>> delete(@PathVariable Long id) {
        hotelService.delete(id);
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success("Hotel deleted", null));
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Upload hotel image (ADMIN only)")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasHotelResponse>> uploadImage(
            @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(hotelService.uploadImage(id, file)));
    }
}
