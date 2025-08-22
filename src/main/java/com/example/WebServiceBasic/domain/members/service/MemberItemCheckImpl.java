package com.example.WebServiceBasic.domain.members.service;

import com.example.WebServiceBasic.domain.auth.role.Role;
import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.members.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberItemCheckImpl implements MemberItemCheck{

    @Autowired
    MemberRepository memberRepository;


    @Override
    public Member getUserIdRead(String memberId){

        //내용 가져오기
        Member member = memberRepository.findByMemberId(memberId).orElse(null);
        return member;
    }

    @Override
    public boolean checkUserId(String userId){

        return memberRepository.existsByMemberId(userId);
    }


    @Override
    public boolean checkEmail(String email) {

        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean checkEmailIsNotId(String email, long id){
        return memberRepository.existsByEmailAndIdNot(email, id);
    }


    @Override
    public int userEdit__(MemberDto dto){

        //권한 가져오기
        Role role = Role.valueOf(dto.getRole());

        //엔티티로 넘기기
        Member entity = dto.toEntity(role);

        //내용확인후 있으면 수정하기
        int updatedCount = memberRepository.updateUserNative(entity.getPassword(),
                entity.getEmail(), entity.getUsername(), entity.getMemberId());

        return updatedCount;
    }

}







