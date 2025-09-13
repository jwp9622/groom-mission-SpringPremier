package com.example.SpringPremier.domain.auth.dto;

import com.example.SpringPremier.global.adapter.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserInfoDto {
    private String username;
    private String username2;
    private List<String> roles;

    // CustomUserDetails에서 직접 생성
    public UserInfoDto(CustomUserDetails userDetails) {
        this.username = userDetails.getUsername();
        this.username2 = userDetails.getUsername2();
        this.roles = userDetails.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());
    }
}