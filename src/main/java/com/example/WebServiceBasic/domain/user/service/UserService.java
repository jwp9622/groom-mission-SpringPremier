package com.example.WebServiceBasic.domain.user.service;

import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;

public interface UserService {
    int userEdit(MemberDto dto);
    Member userDelete(String memberId);

    Member findMemberId(String memberId);
    Member userMemberIdPasswordCheck(String memberId, String password);
}
