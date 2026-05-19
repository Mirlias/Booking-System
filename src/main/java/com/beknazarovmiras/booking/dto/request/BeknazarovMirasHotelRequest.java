package com.beknazarovmiras.booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasHotelRequest {
    @NotBlank(message = "Hotel name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Address is required")
    private String address;

    private String description;

    @Min(1) @Max(5)
    private Integer starRating;

    @Email
    private String contactEmail;

    private String contactPhone;
}
