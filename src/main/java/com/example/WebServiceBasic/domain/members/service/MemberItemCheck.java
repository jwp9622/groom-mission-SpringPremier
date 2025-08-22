package com.example.WebServiceBasic.domain.members.service;

import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;

public interface MemberItemCheck {

    //아이디정보 가져오기
    Member getUserIdRead(String memberId);

    //아이디 중복 확인
    boolean checkUserId(String userId);

    //수정시 이메일 중복 중복 확인
    boolean checkEmail(String email);

    //수정시 이메일 중복 중복 확인
    boolean checkEmailIsNotId(String email, long id);

    //로그인한 사용자 정보, 수정
    int userEdit__(MemberDto dto);

}
