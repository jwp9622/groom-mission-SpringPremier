package com.example.SpringPremier.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;

}

