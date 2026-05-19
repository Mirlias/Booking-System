package com.beknazarovmiras.booking.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasHotelResponse {
    private Long id;
    private String name;
    private String city;
    private String address;
    private String description;
    private Integer starRating;
    private String imageUrl;
    private String contactEmail;
    private String contactPhone;
    private Double averageRating;
    private LocalDateTime createdAt;
}
