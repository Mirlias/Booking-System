package com.beknazarovmiras.booking.dto.response;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    public static <T> BeknazarovMirasApiResponse<T> success(T data) {
        return BeknazarovMirasApiResponse.<T>builder().success(true).message("OK").data(data).build();
    }
    public static <T> BeknazarovMirasApiResponse<T> success(String message, T data) {
        return BeknazarovMirasApiResponse.<T>builder().success(true).message(message).data(data).build();
    }
    public static <T> BeknazarovMirasApiResponse<T> error(String message) {
        return BeknazarovMirasApiResponse.<T>builder().success(false).message(message).build();
    }
}
