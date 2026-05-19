package com.beknazarovmiras.booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasBookingRequest {
    @NotNull
    private Long roomId;

    @NotNull @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    private String specialRequests;
}
