package com.example.WebServiceBasic.auth.service;

import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void userEdit_회원_정보_수정(){
        MemberDto dto = new MemberDto();
        dto.setMemberId("aaaa");
        dto.setPassword("1111");
        dto.setEmail("aaaa@naver.com");
        dto.setUsername("사용자a");
        dto.setRole("USER");

        int count = userService.userEdit(dto);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void userDelete_회원_탈퇴(){

        String memberId = "aaaa";

        Member member = userService.userDelete(memberId);
        assertNotNull(member);


    }

}
