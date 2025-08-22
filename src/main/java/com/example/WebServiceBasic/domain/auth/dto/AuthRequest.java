package com.example.WebServiceBasic.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class AuthRequest {
    private String username;
    private String username2;
    private String password;
    private String role;
}