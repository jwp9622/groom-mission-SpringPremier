package com.example.WebServiceBasic.members;

import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.auth.role.Role;
import com.example.WebServiceBasic.domain.members.repository.MemberRepository;
import com.example.WebServiceBasic.domain.members.service.MemberItemCheck;
import com.example.WebServiceBasic.domain.members.service.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceTest {

    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    MemberItemCheck memberItemCheck;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void getList_회원_목록(){

        Role role_admin = Role.ROLE_ADMIN;
        Role role_user = Role.ROLE_USER;

        Member a = new Member(5L,"dddd", role_user, "$2a$10$xNWUJuJYJPWF93byVkHeVeQgItrNn9kkMRGa3Vy785EVbxWqXJG8S", "dddd@naver.com","사용자d",LocalDate.parse("2025-05-05").atStartOfDay(), null, null);
        Member b = new Member(4L,"cccc", role_user, "$2a$10$/XijRgIqUBqcTuz.tvvmZOwLyRVulviKvQEPIuptgzR.xGLG8K8Ci", "cccc@naver.com","사용자c",LocalDate.parse("2025-05-04").atStartOfDay(), null, null);
        Member c = new Member(3L,"bbbb", role_user, "$2a$10$pX.uNsLWcVg7ZYoVG1f6E.ntcfyovpsEVlGjos8BPMRpHni0bLuXy", "bbbb@naver.com","사용자b",LocalDate.parse("2025-05-03").atStartOfDay(), null, null);
        Member d = new Member(2L,"aaaa", role_user, "$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq", "aaaa@naver.com","사용자a",LocalDate.parse("2025-05-02").atStartOfDay(), null, null);
        Member e = new Member(1L,"admin", role_admin, "$2a$10$i1A8fhv9bo24eVNdU.ENeO.AE7kVlxYvhwRKz4NLFXoJw0UZLOvnC", "admin@naver.com","관리자",LocalDate.parse("2025-05-01").atStartOfDay(), null, null);

        List<Member> expected = Arrays.asList(a,b,c,d,e);

        //실제데이터 5개까지 가져오기
        String keyword = "";
        int page = 1;
        int size = 5;
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("id")));
        //List<Member> actual = memberService.getList(keyword, page, size).getContent();
        List<Member> actual = memberRepository.findByUsernameContaining(keyword, pageable).getContent();

/*
        for(int i=0;i<expected.size();i++){
            System.out.println( expected.get(i).getId()+"_"+expected.get(i).getMemberId()+"_"+expected.get(i).getRole()+"_"+
                    expected.get(i).getPassword()+"_"+expected.get(i).getEmail()+"_"+expected.get(i).getUsername()+"_"+
                    expected.get(i).getUpdatedAt()+"_"+expected.get(i).getUpdatedAt());
        }

        for(int i=0;i<actual.size();i++){
            System.out.println( actual.get(i).getId()+"_"+actual.get(i).getMemberId()+"_"+actual.get(i).getRole()+"_"+
                    actual.get(i).getPassword()+"_"+actual.get(i).getEmail()+"_"+actual.get(i).getUsername()+"_"+
                    actual.get(i).getUpdatedAt()+"_"+actual.get(i).getUpdatedAt());
        }
*/
        //값 비교하기
        assertEquals(expected.toString(),  actual.toString());
        //assertIterableEquals(expected, actual);

    }

    //회원 목록 검색어
    @Test
    void getList_회원_목록_검색(){

        //임의 데이터 생성
        Role role_admin = Role.ROLE_ADMIN;
        Role role_user = Role.ROLE_USER;

        //Member a = new Member(5L,"dddd", role_user, "4444", "dddd@naver.com","사용자d",LocalDate.parse("2025-05-05").atStartOfDay(), null);
        //Member b = new Member(4L,"cccc", role_user, "3333", "cccc@naver.com","사용자c",LocalDate.parse("2025-05-04").atStartOfDay(), null);
        //Member c = new Member(3L,"bbbb", role_user, "2222", "bbbb@naver.com","사용자b",LocalDate.parse("2025-05-03").atStartOfDay(), null);
        //Member d = new Member(2L,"aaaa", role_user, "1111", "aaaa@naver.com","사용자a",LocalDate.parse("2025-05-02").atStartOfDay(), null);
        Member e = new Member(1L,"admin", role_admin, "$2a$10$i1A8fhv9bo24eVNdU.ENeO.AE7kVlxYvhwRKz4NLFXoJw0UZLOvnC", "admin@naver.com","관리자",LocalDate.parse("2025-05-01").atStartOfDay(), null, null);


        List<Member> expected = Arrays.asList(e);

        //실제데이터 5개까지 가져오기
        String keyword = "관리자";
        int page = 1;
        int size = 5;
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("id")));
        //List<Member> actual = memberService.getListList(keyword, page, size, pageable).getContent();
        List<Member> actual = memberRepository.findByUsernameContaining(keyword, pageable).getContent();


        //값 비교하기
        assertEquals(expected.toString(),  actual.toString());

    }



    @Test
    void getUserIdRead_아이디_정보_가져오기(){
        
        //임의 데이터 생성
        Role role_admin = Role.ROLE_ADMIN;
        Role role_user = Role.ROLE_USER;

        Member a = new Member(5L,"dddd", role_user, "$2a$10$xNWUJuJYJPWF93byVkHeVeQgItrNn9kkMRGa3Vy785EVbxWqXJG8S", "dddd@naver.com","사용자d",LocalDate.parse("2025-05-05").atStartOfDay(), null, null);
        //Member b = new Member(4L,"cccc", role_user, "3333", "cccc@naver.com","사용자c",LocalDate.parse("2025-05-04").atStartOfDay(), null);
        //Member c = new Member(3L,"bbbb", role_user, "2222", "bbbb@naver.com","사용자b",LocalDate.parse("2025-05-03").atStartOfDay(), null);
        //Member d = new Member(2L,"aaaa", role_user, "1111", "aaaa@naver.com","사용자a",LocalDate.parse("2025-05-02").atStartOfDay(), null);
        //Member e = new Member(1L,"admin", role_admin, "1234", "admin@naver.com","관리자",LocalDate.parse("2025-05-01").atStartOfDay(), null);
        Member expected = a;

        //실제 데이터 가져오기
        String memberId = "dddd";
        Member actual = memberItemCheck.getUserIdRead(memberId);

        //값 비교
        assertEquals(expected.toString(),  actual.toString());

    }



    @Test
    void getRead_회원_상세(){
        
        //임의 데이터 생성
        Role role_admin = Role.ROLE_ADMIN;
        Role role_user = Role.ROLE_USER;

        Member a = new Member(5L,"dddd", role_user, "$2a$10$xNWUJuJYJPWF93byVkHeVeQgItrNn9kkMRGa3Vy785EVbxWqXJG8S", "dddd@naver.com","사용자d",LocalDate.parse("2025-05-05").atStartOfDay(), null, null);
        //Member b = new Member(4L,"cccc", role_user, "3333", "cccc@naver.com","사용자c",LocalDate.parse("2025-05-04").atStartOfDay(), null);
        //Member c = new Member(3L,"bbbb", role_user, "2222", "bbbb@naver.com","사용자b",LocalDate.parse("2025-05-03").atStartOfDay(), null);
        //Member d = new Member(2L,"aaaa", role_user, "1111", "aaaa@naver.com","사용자a",LocalDate.parse("2025-05-02").atStartOfDay(), null);
        //Member e = new Member(1L,"admin", role_admin, "1234", "admin@naver.com","관리자",LocalDate.parse("2025-05-01").atStartOfDay(), null);
        Member expected = a;

        //실제 값 가져오기
        Long id = 5L;
        Member actual = memberService.getRead(5L);
        
        //값 비교
        assertEquals(expected.toString(),  actual.toString());
    }


    @Test
    void create_회원_등록_하기(){

        //임의 데이터 생성
        Role role_admin = Role.ROLE_ADMIN;
        Role role_user = Role.ROLE_USER;
        
        MemberDto dto = new MemberDto();
        dto.setMemberId("abcd");
        dto.setRole("ROLE_USER");
        dto.setPassword("$2a$10$i1A8fhv9bo24eVNdU.ENeO.AE7kVlxYvhwRKz4NLFXoJw0UZLOvnC");
        dto.setEmail("test@naver.com");
        dto.setUpdatedAt(LocalDate.parse("2026-06-07").atStartOfDay());
        
        //임의 데이터 등록
        Member actual = memberService.create(dto);
        
        //등록여부 체크
        assertNotNull(actual.getId());

    }

    @Test
    void getEditForm_회원_수정폼(){

        //실제데이터 가져오기 확인
        Long id = 5L;
        Member actual = memberService.getEditForm(id);
        
        //데이터 비교확인
        assertNotNull(actual.getId());

    }


    @Test
    void edit_회원_업데이트(){

        //임의 데이터 생성
        Role role_admin = Role.ROLE_ADMIN;
        Role role_user = Role.ROLE_USER;

        MemberDto dto = new MemberDto();
        dto.setId(5L);
        dto.setMemberId("aaaa");
        dto.setRole("ROLE_USER");
        dto.setPassword("1111");
        dto.setEmail("aaaa@naver.com");
        dto.setUpdatedAt(LocalDate.parse("2026-06-07").atStartOfDay());

        //Member a = new Member(5L,"dddd", role_user, "4444", "dddd@naver.com","사용자d",LocalDate.parse("2025-05-05").atStartOfDay(), null);

        //실제 데이터 수정
        Member actual = memberService.edit(dto);

        //등록여부 체크
        assertThat(actual.getEmail()).isEqualTo(dto.getEmail());

    }


    @Test
    void delete_삭제(){

        Long id = 5L;
        Member actual = memberService.delete(id);

        assertNotNull(actual.getId());


    }



}
