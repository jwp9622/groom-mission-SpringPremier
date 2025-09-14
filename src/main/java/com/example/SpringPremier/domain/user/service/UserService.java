package com.example.SpringPremier.domain.user.service;

import com.example.SpringPremier.domain.members.dto.MemberDto;
import com.example.SpringPremier.domain.members.entity.Member;

public interface UserService {
    int userEdit(MemberDto dto);
    Member userDelete(String memberId);

    Member findMemberId(String memberId);
    Member userMemberIdPasswordCheck(String memberId, String password);
}
