package com.beknazarovmiras.booking.dto.response;
import com.beknazarovmiras.booking.entity.BeknazarovMirasRoom;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasRoomResponse {
    private Long id;
    private String roomNumber;
    private BeknazarovMirasRoom.RoomType roomType;
    private Integer capacity;
    private BigDecimal pricePerNight;
    private String description;
    private String imageUrl;
    private Boolean available;
    private Long hotelId;
    private String hotelName;
    private LocalDateTime createdAt;
}
