package com.beknazarovmiras.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean available;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private BeknazarovMirasHotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<BeknazarovMirasBooking> bookings = new ArrayList<>();

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum RoomType {
        SINGLE, DOUBLE, SUITE, DELUXE, FAMILY
    }
}
