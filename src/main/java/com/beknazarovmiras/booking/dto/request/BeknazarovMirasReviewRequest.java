package com.beknazarovmiras.booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasReviewRequest {
    @NotNull @Min(1) @Max(5)
    private Integer rating;

    @NotBlank @Size(min = 10, max = 1000)
    private String comment;

    @NotNull
    private Long hotelId;
}
