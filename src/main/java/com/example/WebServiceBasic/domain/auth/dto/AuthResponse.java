package com.example.WebServiceBasic.domain.auth.dto;

import jakarta.servlet.http.Cookie;
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

