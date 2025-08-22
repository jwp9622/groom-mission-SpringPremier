package com.example.WebServiceBasic.domain.auth.service;

import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.members.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //로그인 폼
    public Member getLoginForm(){
        return null;
    }

    //로그인 처리
    public Member login(MemberDto dto, HttpServletRequest http){


        Member member = memberRepository.findByMemberId(dto.getMemberId()).orElse(null);

        if(member == null) return null;

        if(member != null){
            boolean chkPassword = bCryptPasswordEncoder.matches(dto.getPassword(), member.getPassword());
            if(!chkPassword ) return null;
        }

        //Member member = memberRepository.findByMemberId(dto.getMemberId())
        //        .filter(member1 -> member1.getPassword().equals(dto.getPassword()))
        //       .orElse(null);


        //세션등록
        HttpSession session = http.getSession();

        // 세션에 사용자 정보 저장
        session.setMaxInactiveInterval(60 * 30);
        session.setAttribute("username", member.getUsername());
        session.setAttribute("memberId", member.getMemberId());
        session.setAttribute("role", String.valueOf(member.getRole()));


        return member;
    }

    //로그아웃
    public String logout(HttpServletRequest http){

        HttpSession session = http.getSession(false);
        if( session !=null){
            session.invalidate();
        }
        return null;
    }

}
