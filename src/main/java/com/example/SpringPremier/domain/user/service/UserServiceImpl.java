package com.example.SpringPremier.domain.user.service;

import com.example.SpringPremier.domain.members.dto.MemberDto;
import com.example.SpringPremier.domain.members.entity.Member;
import com.example.SpringPremier.domain.members.repository.MemberRepository;
import com.example.SpringPremier.domain.auth.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private Role role;

    //사용자 정보, 수정
    public int userEdit(MemberDto dto){

        //비밀번호 암호화
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));

        //엔티티로 넘기기
        Member entity = dto.toEntity(role);

        //내용확인후 있으면 수정하기
        int updatedCount = memberRepository.updateUserNative(entity.getPassword(),
                entity.getEmail(), entity.getUsername(), entity.getMemberId());

        return updatedCount;
    }

    //회원 삭제
    @Transactional
    public Member userDelete(String memberId){

        Member member = memberRepository.findByMemberId(memberId).orElse(null);
        if(member != null){
            member.setPassword("");
            member.setEmail("");
            member.setUsername("");
            member.setExpiredAt(LocalDateTime.now());
            memberRepository.save(member);
            //memberRepository.delete(member);
            return member;
        }else{
            return null;
        }
    }

    @Override
    public Member findMemberId(String memberId) {

        return memberRepository.findByMemberId(memberId).orElse(null);
    }

    //삭제시 비밀번호 확인
    @Override
    public Member userMemberIdPasswordCheck(String memberId, String password) {
        Member member = memberRepository.findByMemberIdAndPassword(memberId, password).orElse(null);

        return member;
    }

}
