package com.beknazarovmiras.booking.controller;

import com.beknazarovmiras.booking.dto.request.*;
import com.beknazarovmiras.booking.dto.response.*;
import com.beknazarovmiras.booking.service.BeknazarovMirasAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Registration, Login, Token refresh")
public class BeknazarovMirasAuthController {

    private final BeknazarovMirasAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasAuthResponse>> register(
            @Valid @RequestBody BeknazarovMirasRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BeknazarovMirasApiResponse.success("Registered successfully", authService.register(request)));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasAuthResponse>> login(
            @Valid @RequestBody BeknazarovMirasLoginRequest request) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(authService.login(request)));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<BeknazarovMirasApiResponse<BeknazarovMirasAuthResponse>> refresh(
            @RequestParam String refreshToken) {
        return ResponseEntity.ok(BeknazarovMirasApiResponse.success(authService.refreshToken(refreshToken)));
    }
}
