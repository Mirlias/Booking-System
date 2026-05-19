package com.beknazarovmiras.booking.service.impl;

import com.beknazarovmiras.booking.dto.request.*;
import com.beknazarovmiras.booking.dto.response.*;
import com.beknazarovmiras.booking.entity.*;
import com.beknazarovmiras.booking.exception.BeknazarovMirasBadRequestException;
import com.beknazarovmiras.booking.mapper.BeknazarovMirasBookingMapper;
import com.beknazarovmiras.booking.repository.*;
import com.beknazarovmiras.booking.security.BeknazarovMirasUserDetailsImpl;
import com.beknazarovmiras.booking.service.BeknazarovMirasAuthService;
import com.beknazarovmiras.booking.util.BeknazarovMirasJwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeknazarovMirasAuthServiceImpl implements BeknazarovMirasAuthService {

    private final BeknazarovMirasUserRepository userRepository;
    private final BeknazarovMirasRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final BeknazarovMirasJwtUtil jwtUtil;
    private final BeknazarovMirasBookingMapper mapper;

    @Override
    @Transactional
    public BeknazarovMirasAuthResponse register(BeknazarovMirasRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BeknazarovMirasBadRequestException("Email already registered: " + request.getEmail());
        }
        BeknazarovMirasRole userRole = roleRepository
                .findByName(BeknazarovMirasRole.RoleName.ROLE_USER)
                .orElseGet(() -> roleRepository.save(
                        BeknazarovMirasRole.builder().name(BeknazarovMirasRole.RoleName.ROLE_USER).build()));

        BeknazarovMirasUser user = BeknazarovMirasUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        BeknazarovMirasUserDetailsImpl userDetails = new BeknazarovMirasUserDetailsImpl(user);
        return buildAuthResponse(userDetails, user);
    }

    @Override
    public BeknazarovMirasAuthResponse login(BeknazarovMirasLoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        BeknazarovMirasUser user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        BeknazarovMirasUserDetailsImpl userDetails = new BeknazarovMirasUserDetailsImpl(user);
        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(userDetails, user);
    }

    @Override
    public BeknazarovMirasAuthResponse refreshToken(String refreshToken) {
        String email = jwtUtil.extractUsername(refreshToken);
        BeknazarovMirasUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BeknazarovMirasBadRequestException("Invalid refresh token"));
        BeknazarovMirasUserDetailsImpl userDetails = new BeknazarovMirasUserDetailsImpl(user);
        if (!jwtUtil.isTokenValid(refreshToken, userDetails)) {
            throw new BeknazarovMirasBadRequestException("Refresh token expired");
        }
        return buildAuthResponse(userDetails, user);
    }

    private BeknazarovMirasAuthResponse buildAuthResponse(UserDetails userDetails, BeknazarovMirasUser user) {
        return BeknazarovMirasAuthResponse.builder()
                .accessToken(jwtUtil.generateToken(userDetails))
                .refreshToken(jwtUtil.generateRefreshToken(userDetails))
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .user(mapper.toUserResponse(user))
                .build();
    }
}
