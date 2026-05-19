package com.beknazarovmiras.booking.controller;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasReviewRequest;
import com.beknazarovmiras.booking.dto.response.*;
import com.beknazarovmiras.booking.service.BeknazarovMirasReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Hotel reviews")
public class BeknazarovMirasReviewController {

    private final BeknazarovMirasReviewService reviewService;

    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "Get reviews for a hotel with pagination and sorting")
    public ResponseEntity<BeknazarovMirasApiResponse<Page<BeknazarovMirasReviewResponse>>> getByHotel(
            @PathVariable Long hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(reviewService.getByHotel(hotelId, pageable)));
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a review")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasReviewResponse>> create(
            @Valid @RequestBody BeknazarovMirasReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BeknazarovMirasApiResponse.success("Review created",
                        reviewService.create(request, userDetails.getUsername())));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete a review")
    public ResponseEntity<BeknazarovMirasApiResponse<Void>> delete(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.delete(id, userDetails.getUsername());
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success("Review deleted", null));
    }
}
