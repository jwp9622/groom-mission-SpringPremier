package com.example.SpringPremier.domain.auth.service;

import com.example.SpringPremier.domain.members.dto.MemberDto;
import com.example.SpringPremier.domain.members.entity.Member;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    Member getLoginForm();
    Member login(MemberDto dto, HttpServletRequest http);
    String logout(HttpServletRequest http);

}
