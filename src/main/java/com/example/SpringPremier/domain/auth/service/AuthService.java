package com.example.SpringPremier.domain.auth.service;

import com.example.SpringPremier.domain.auth.dto.AuthRequest;
import com.example.SpringPremier.domain.auth.dto.AuthResponse;
import com.example.SpringPremier.domain.members.dto.MemberDto;
import com.example.SpringPremier.domain.members.entity.Member;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public Member register(MemberDto dto);
    public AuthResponse login(AuthRequest request);
    public void logout(HttpServletRequest request);
    public AuthResponse reissue(String refreshToken);

}
