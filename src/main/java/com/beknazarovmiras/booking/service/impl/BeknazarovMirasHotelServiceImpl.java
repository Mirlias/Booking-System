package com.beknazarovmiras.booking.service.impl;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasHotelRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasHotelResponse;
import com.beknazarovmiras.booking.entity.BeknazarovMirasHotel;
import com.beknazarovmiras.booking.exception.BeknazarovMirasNotFoundException;
import com.beknazarovmiras.booking.mapper.BeknazarovMirasBookingMapper;
import com.beknazarovmiras.booking.repository.*;
import com.beknazarovmiras.booking.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeknazarovMirasHotelServiceImpl implements BeknazarovMirasHotelService {

    private final BeknazarovMirasHotelRepository hotelRepository;
    private final BeknazarovMirasReviewRepository reviewRepository;
    private final BeknazarovMirasFileService fileService;
    private final BeknazarovMirasBookingMapper mapper;

    @Override
    @Transactional
    public BeknazarovMirasHotelResponse create(BeknazarovMirasHotelRequest request) {
        BeknazarovMirasHotel hotel = BeknazarovMirasHotel.builder()
                .name(request.getName()).city(request.getCity())
                .address(request.getAddress()).description(request.getDescription())
                .starRating(request.getStarRating()).contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone()).build();
        hotelRepository.save(hotel);
        log.info("Hotel created: {}", hotel.getName());
        return mapper.toHotelResponse(hotel, null);
    }

    @Override
    public BeknazarovMirasHotelResponse getById(Long id) {
        BeknazarovMirasHotel hotel = findById(id);
        Double avg = reviewRepository.findAverageRatingByHotelId(id);
        return mapper.toHotelResponse(hotel, avg);
    }

    @Override
    public Page<BeknazarovMirasHotelResponse> getAll(Pageable pageable) {
        return hotelRepository.findAll(pageable)
                .map(h -> mapper.toHotelResponse(h, reviewRepository.findAverageRatingByHotelId(h.getId())));
    }

    @Override
    public Page<BeknazarovMirasHotelResponse> search(String name, String city,
            Integer minStars, Integer maxStars, Pageable pageable) {
        return hotelRepository.searchHotels(name, city, minStars, maxStars, pageable)
                .map(h -> mapper.toHotelResponse(h, reviewRepository.findAverageRatingByHotelId(h.getId())));
    }

    @Override
    @Transactional
    public BeknazarovMirasHotelResponse update(Long id, BeknazarovMirasHotelRequest request) {
        BeknazarovMirasHotel hotel = findById(id);
        hotel.setName(request.getName()); hotel.setCity(request.getCity());
        hotel.setAddress(request.getAddress()); hotel.setDescription(request.getDescription());
        hotel.setStarRating(request.getStarRating()); hotel.setContactEmail(request.getContactEmail());
        hotel.setContactPhone(request.getContactPhone());
        hotelRepository.save(hotel);
        return mapper.toHotelResponse(hotel, reviewRepository.findAverageRatingByHotelId(id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        BeknazarovMirasHotel hotel = findById(id);
        hotelRepository.delete(hotel);
        log.info("Hotel deleted: {}", id);
    }

    @Override
    @Transactional
    public BeknazarovMirasHotelResponse uploadImage(Long id, MultipartFile file) {
        BeknazarovMirasHotel hotel = findById(id);
        String url = fileService.uploadFile(file, "hotels");
        hotel.setImageUrl(url);
        hotelRepository.save(hotel);
        return mapper.toHotelResponse(hotel, reviewRepository.findAverageRatingByHotelId(id));
    }

    @Async("taskExecutor")
    public CompletableFuture<Long> countHotelsAsync() {
        long count = hotelRepository.count();
        log.info("Async hotel count: {}", count);
        return CompletableFuture.completedFuture(count);
    }

    private BeknazarovMirasHotel findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("Hotel not found: " + id));
    }
}
