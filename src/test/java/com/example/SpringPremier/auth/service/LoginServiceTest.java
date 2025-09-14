package com.example.SpringPremier.auth.service;


import com.example.SpringPremier.domain.auth.service.LoginService;
import com.example.SpringPremier.domain.members.dto.MemberDto;
import com.example.SpringPremier.domain.members.entity.Member;
import com.example.SpringPremier.domain.members.repository.MemberRepository;
import com.example.SpringPremier.domain.members.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LoginServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    LoginService loginService;

    @Test
    void login_로그인_가능_여부(){

        //테스트 아이디 생성
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId("aaaa");
        memberDto.setPassword("1111");

        //http 생성
        HttpServletRequest http = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(http.getSession()).thenReturn(session);

        //로그인 호출
        Member actual = loginService.login(memberDto, http);

        //username 있는지 여부 체크
        assertNotNull(actual.getUsername());

    }

    @Test
    void logout(){
        //테스트 아이디 생성
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId("aaaa");
        memberDto.setPassword("1111");

        //http 생성
        HttpServletRequest http = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(http.getSession()).thenReturn(session);

        //로그인 호출
        Member actual = loginService.login(memberDto, http);

        //로그아웃 호출
        String val = loginService.logout(http);

        //return 값이  null여부 체크
        assertNull(val);


    }

}
