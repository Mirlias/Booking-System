package com.beknazarovmiras.booking.exception;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> fieldErrors;
}
