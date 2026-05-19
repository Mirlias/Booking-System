package com.beknazarovmiras.booking.service;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasHotelRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasHotelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BeknazarovMirasHotelService {
    BeknazarovMirasHotelResponse create(BeknazarovMirasHotelRequest request);
    BeknazarovMirasHotelResponse getById(Long id);
    Page<BeknazarovMirasHotelResponse> getAll(Pageable pageable);
    Page<BeknazarovMirasHotelResponse> search(String name, String city, Integer minStars, Integer maxStars, Pageable pageable);
    BeknazarovMirasHotelResponse update(Long id, BeknazarovMirasHotelRequest request);
    void delete(Long id);
    BeknazarovMirasHotelResponse uploadImage(Long id, MultipartFile file);
}
