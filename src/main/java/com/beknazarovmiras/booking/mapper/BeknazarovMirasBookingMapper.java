package com.beknazarovmiras.booking.mapper;

import com.beknazarovmiras.booking.dto.response.*;
import com.beknazarovmiras.booking.entity.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class BeknazarovMirasBookingMapper {

    public BeknazarovMirasUserResponse toUserResponse(BeknazarovMirasUser user) {
        return BeknazarovMirasUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().name()).collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .build();
    }

    public BeknazarovMirasHotelResponse toHotelResponse(BeknazarovMirasHotel hotel, Double avgRating) {
        return BeknazarovMirasHotelResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .city(hotel.getCity())
                .address(hotel.getAddress())
                .description(hotel.getDescription())
                .starRating(hotel.getStarRating())
                .imageUrl(hotel.getImageUrl())
                .contactEmail(hotel.getContactEmail())
                .contactPhone(hotel.getContactPhone())
                .averageRating(avgRating)
                .createdAt(hotel.getCreatedAt())
                .build();
    }

    public BeknazarovMirasRoomResponse toRoomResponse(BeknazarovMirasRoom room) {
        return BeknazarovMirasRoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .pricePerNight(room.getPricePerNight())
                .description(room.getDescription())
                .imageUrl(room.getImageUrl())
                .available(room.getAvailable())
                .hotelId(room.getHotel().getId())
                .hotelName(room.getHotel().getName())
                .createdAt(room.getCreatedAt())
                .build();
    }

    public BeknazarovMirasBookingResponse toBookingResponse(BeknazarovMirasBooking booking) {
        return BeknazarovMirasBookingResponse.builder()
                .id(booking.getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .specialRequests(booking.getSpecialRequests())
                .userId(booking.getUser().getId())
                .userEmail(booking.getUser().getEmail())
                .roomId(booking.getRoom().getId())
                .roomNumber(booking.getRoom().getRoomNumber())
                .hotelName(booking.getRoom().getHotel().getName())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    public BeknazarovMirasReviewResponse toReviewResponse(BeknazarovMirasReview review) {
        return BeknazarovMirasReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userId(review.getUser().getId())
                .userFullName(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                .hotelId(review.getHotel().getId())
                .hotelName(review.getHotel().getName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
