package com.beknazarovmiras.booking.service.impl;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasRoomRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasRoomResponse;
import com.beknazarovmiras.booking.entity.*;
import com.beknazarovmiras.booking.exception.BeknazarovMirasNotFoundException;
import com.beknazarovmiras.booking.mapper.BeknazarovMirasBookingMapper;
import com.beknazarovmiras.booking.repository.*;
import com.beknazarovmiras.booking.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeknazarovMirasRoomServiceImpl implements BeknazarovMirasRoomService {

    private final BeknazarovMirasRoomRepository roomRepository;
    private final BeknazarovMirasHotelRepository hotelRepository;
    private final BeknazarovMirasFileService fileService;
    private final BeknazarovMirasBookingMapper mapper;

    @Override
    @Transactional
    public BeknazarovMirasRoomResponse create(Long hotelId, BeknazarovMirasRoomRequest request) {
        BeknazarovMirasHotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("Hotel not found: " + hotelId));
        BeknazarovMirasRoom room = BeknazarovMirasRoom.builder()
                .roomNumber(request.getRoomNumber()).roomType(request.getRoomType())
                .capacity(request.getCapacity()).pricePerNight(request.getPricePerNight())
                .description(request.getDescription()).available(request.getAvailable())
                .hotel(hotel).build();
        roomRepository.save(room);
        log.info("Room created: {} in hotel {}", room.getRoomNumber(), hotelId);
        return mapper.toRoomResponse(room);
    }

    @Override
    public BeknazarovMirasRoomResponse getById(Long id) {
        return mapper.toRoomResponse(findById(id));
    }

    @Override
    public Page<BeknazarovMirasRoomResponse> getByHotel(Long hotelId, Pageable pageable) {
        return roomRepository.findByHotelId(hotelId, pageable).map(mapper::toRoomResponse);
    }

    @Override
    public Page<BeknazarovMirasRoomResponse> filterRooms(Long hotelId, BeknazarovMirasRoom.RoomType type,
            BigDecimal minPrice, BigDecimal maxPrice, Integer minCapacity, Pageable pageable) {
        return roomRepository.filterRooms(hotelId, type, minPrice, maxPrice, minCapacity, pageable)
                .map(mapper::toRoomResponse);
    }

    @Override
    public List<BeknazarovMirasRoomResponse> getAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRooms(hotelId, checkIn, checkOut)
                .stream().map(mapper::toRoomResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BeknazarovMirasRoomResponse update(Long id, BeknazarovMirasRoomRequest request) {
        BeknazarovMirasRoom room = findById(id);
        room.setRoomNumber(request.getRoomNumber()); room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity()); room.setPricePerNight(request.getPricePerNight());
        room.setDescription(request.getDescription()); room.setAvailable(request.getAvailable());
        return mapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        roomRepository.delete(findById(id));
        log.info("Room deleted: {}", id);
    }

    @Override
    @Transactional
    public BeknazarovMirasRoomResponse uploadImage(Long id, MultipartFile file) {
        BeknazarovMirasRoom room = findById(id);
        room.setImageUrl(fileService.uploadFile(file, "rooms"));
        return mapper.toRoomResponse(roomRepository.save(room));
    }

    private BeknazarovMirasRoom findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("Room not found: " + id));
    }
}
