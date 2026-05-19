package com.beknazarovmiras.booking.dto.response;
import com.beknazarovmiras.booking.entity.BeknazarovMirasBooking;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasBookingResponse {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalPrice;
    private BeknazarovMirasBooking.BookingStatus status;
    private String specialRequests;
    private Long userId;
    private String userEmail;
    private Long roomId;
    private String roomNumber;
    private String hotelName;
    private LocalDateTime createdAt;
}
