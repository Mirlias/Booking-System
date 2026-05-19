package com.beknazarovmiras.booking.service;

import com.beknazarovmiras.booking.dto.request.*;
import com.beknazarovmiras.booking.dto.response.*;

public interface BeknazarovMirasAuthService {
    BeknazarovMirasAuthResponse register(BeknazarovMirasRegisterRequest request);
    BeknazarovMirasAuthResponse login(BeknazarovMirasLoginRequest request);
    BeknazarovMirasAuthResponse refreshToken(String refreshToken);
}
