package com.example.WebServiceBasic.domain.members.service;

import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MemberService {
    List<MemberDto> pagenation(Page<Member> list, int total, int page, int size, String keyword);
    Page<MemberDto> getList(String keyword, int page, int size);
    Member getRead(Long id);
    Member create(MemberDto dto);
    Member getEditForm(Long id);
    Member edit(MemberDto dto);
    Member delete(Long id);

}
