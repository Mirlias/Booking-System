package com.beknazarovmiras.booking.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeknazarovMirasUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatarUrl;
    private Set<String> roles;
    private LocalDateTime createdAt;
}
