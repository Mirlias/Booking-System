package com.beknazarovmiras.booking.service.impl;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasReviewRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasReviewResponse;
import com.beknazarovmiras.booking.entity.*;
import com.beknazarovmiras.booking.exception.*;
import com.beknazarovmiras.booking.mapper.BeknazarovMirasBookingMapper;
import com.beknazarovmiras.booking.repository.*;
import com.beknazarovmiras.booking.service.BeknazarovMirasReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeknazarovMirasReviewServiceImpl implements BeknazarovMirasReviewService {

    private final BeknazarovMirasReviewRepository reviewRepository;
    private final BeknazarovMirasHotelRepository hotelRepository;
    private final BeknazarovMirasUserRepository userRepository;
    private final BeknazarovMirasBookingMapper mapper;

    @Override
    @Transactional
    public BeknazarovMirasReviewResponse create(BeknazarovMirasReviewRequest request, String userEmail) {
        BeknazarovMirasUser user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("User not found"));
        BeknazarovMirasHotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("Hotel not found: " + request.getHotelId()));
        if (reviewRepository.existsByUserIdAndHotelId(user.getId(), hotel.getId())) {
            throw new BeknazarovMirasBadRequestException("You already reviewed this hotel");
        }
        BeknazarovMirasReview review = BeknazarovMirasReview.builder()
                .rating(request.getRating()).comment(request.getComment())
                .user(user).hotel(hotel).build();
        reviewRepository.save(review);
        log.info("Review created for hotel {} by {}", hotel.getId(), userEmail);
        return mapper.toReviewResponse(review);
    }

    @Override
    public Page<BeknazarovMirasReviewResponse> getByHotel(Long hotelId, Pageable pageable) {
        return reviewRepository.findByHotelId(hotelId, pageable).map(mapper::toReviewResponse);
    }

    @Override
    @Transactional
    public void delete(Long id, String userEmail) {
        BeknazarovMirasReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new BeknazarovMirasNotFoundException("Review not found: " + id));
        if (!review.getUser().getEmail().equals(userEmail)) {
            throw new BeknazarovMirasUnauthorizedException("You can only delete your own reviews");
        }
        reviewRepository.delete(review);
    }
}
