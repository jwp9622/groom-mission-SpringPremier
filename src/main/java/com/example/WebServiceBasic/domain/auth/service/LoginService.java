package com.example.WebServiceBasic.domain.auth.service;

import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    Member getLoginForm();
    Member login(MemberDto dto, HttpServletRequest http);
    String logout(HttpServletRequest http);

}
