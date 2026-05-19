package com.beknazarovmiras.booking.service;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasRoomRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasRoomResponse;
import com.beknazarovmiras.booking.entity.BeknazarovMirasRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BeknazarovMirasRoomService {
    BeknazarovMirasRoomResponse create(Long hotelId, BeknazarovMirasRoomRequest request);
    BeknazarovMirasRoomResponse getById(Long id);
    Page<BeknazarovMirasRoomResponse> getByHotel(Long hotelId, Pageable pageable);
    Page<BeknazarovMirasRoomResponse> filterRooms(Long hotelId, BeknazarovMirasRoom.RoomType type,
            BigDecimal minPrice, BigDecimal maxPrice, Integer minCapacity, Pageable pageable);
    List<BeknazarovMirasRoomResponse> getAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut);
    BeknazarovMirasRoomResponse update(Long id, BeknazarovMirasRoomRequest request);
    void delete(Long id);
    BeknazarovMirasRoomResponse uploadImage(Long id, MultipartFile file);
}
