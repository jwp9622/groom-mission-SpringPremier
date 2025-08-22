package com.example.WebServiceBasic.global.adapter;

import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.members.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    //private final AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public CustomUserDetailsService(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LocalDateTime expired = null;

        Member member = memberRepository.findByMemberIdAndExpiredAt(username, expired).orElse(null);

        if (member == null) {
            throw new UsernameNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }else{
            return new CustomUserDetails(member);
        }

    }

}
