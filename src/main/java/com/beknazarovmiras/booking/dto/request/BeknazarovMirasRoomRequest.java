package com.beknazarovmiras.booking.dto.request;

import com.beknazarovmiras.booking.entity.BeknazarovMirasRoom;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasRoomRequest {
    @NotBlank
    private String roomNumber;

    @NotNull
    private BeknazarovMirasRoom.RoomType roomType;

    @NotNull @Min(1)
    private Integer capacity;

    @NotNull @DecimalMin("0.01")
    private BigDecimal pricePerNight;

    private String description;

    private Boolean available = true;
}
