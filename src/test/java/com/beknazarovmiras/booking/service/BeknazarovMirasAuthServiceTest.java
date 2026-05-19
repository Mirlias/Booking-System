package com.beknazarovmiras.booking.service;

import com.beknazarovmiras.booking.dto.request.BeknazarovMirasRegisterRequest;
import com.beknazarovmiras.booking.dto.response.BeknazarovMirasAuthResponse;
import com.beknazarovmiras.booking.entity.*;
import com.beknazarovmiras.booking.mapper.BeknazarovMirasBookingMapper;
import com.beknazarovmiras.booking.repository.*;
import com.beknazarovmiras.booking.service.impl.BeknazarovMirasAuthServiceImpl;
import com.beknazarovmiras.booking.util.BeknazarovMirasJwtUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeknazarovMirasAuthServiceTest {

    @Mock BeknazarovMirasUserRepository userRepository;
    @Mock BeknazarovMirasRoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock AuthenticationManager authManager;
    @Mock BeknazarovMirasJwtUtil jwtUtil;
    @Mock BeknazarovMirasBookingMapper mapper;

    @InjectMocks
    BeknazarovMirasAuthServiceImpl authService;

    @Test
    @DisplayName("Should register a new user successfully")
    void shouldRegisterUser() {
        BeknazarovMirasRegisterRequest request = BeknazarovMirasRegisterRequest.builder()
                .firstName("Miras").lastName("Beknazarov")
                .email("miras@test.com").password("pass123").build();

        BeknazarovMirasRole role = BeknazarovMirasRole.builder()
                .id(1L).name(BeknazarovMirasRole.RoleName.ROLE_USER).build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(jwtUtil.generateToken(any())).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("refresh-token");
        when(mapper.toUserResponse(any())).thenReturn(null);

        BeknazarovMirasAuthResponse result = authService.register(request);

        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("access-token");
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowWhenEmailExists() {
        BeknazarovMirasRegisterRequest request = BeknazarovMirasRegisterRequest.builder()
                .firstName("Miras").lastName("Beknazarov")
                .email("existing@test.com").password("pass123").build();

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already registered");
    }
}
