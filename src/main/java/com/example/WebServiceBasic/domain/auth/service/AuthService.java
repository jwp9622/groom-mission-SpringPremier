package com.example.WebServiceBasic.domain.auth.service;

import com.example.WebServiceBasic.domain.auth.dto.AuthRequest;
import com.example.WebServiceBasic.domain.auth.dto.AuthResponse;
import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public Member register(MemberDto dto);
    public AuthResponse login(AuthRequest request);
    public void logout(HttpServletRequest request);
    public AuthResponse reissue(String refreshToken);

}
