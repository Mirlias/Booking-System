package com.beknazarovmiras.booking.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private Long userId;
    private String userFullName;
    private Long hotelId;
    private String hotelName;
    private LocalDateTime createdAt;
}
