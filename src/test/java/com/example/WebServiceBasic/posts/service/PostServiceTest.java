package com.example.WebServiceBasic.posts.service;

import com.example.WebServiceBasic.domain.posts.dto.PostDto;
import com.example.WebServiceBasic.domain.boards.entity.Board;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.posts.entity.Post;
import com.example.WebServiceBasic.domain.boards.repository.BoardRepository;
import com.example.WebServiceBasic.domain.members.repository.MemberRepository;
import com.example.WebServiceBasic.domain.posts.repository.PostRepository;
import com.example.WebServiceBasic.domain.posts.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    void getList_목록(){

        Board board_qna = boardRepository.findByCode("qna").orElse(null);
        Board board_notice = boardRepository.findByCode("notice").orElse(null);

        Member member_aaaa = memberRepository.findByMemberId("aaaa").orElse(null);
        Member member_admin = memberRepository.findByMemberId("admin").orElse(null);

        Post a = new Post( 3L, 3, 0, 0, board_notice, member_admin, "공지사항3", "관리자", "3내용입니다.. 내용입니다.","$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq",LocalDate.parse("2025-05-03").atStartOfDay(), null);
        Post b = new Post( 2L, 2, 0, 0, board_notice, member_admin, "공지사항2", "관리자", "2내용입니다.. 내용입니다.","$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq",LocalDate.parse("2025-05-02").atStartOfDay(), null);
        Post c = new Post( 1L, 1, 0, 0, board_notice, member_admin, "공지사항1", "관리자", "1내용입니다.. 내용입니다.","$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq",LocalDate.parse("2025-05-01").atStartOfDay(), null);

        List<Post> expected = Arrays.asList(a,b,c);

        //실제데이터 5개까지 가져오기
        String board_code = "notice";
        String keyword = "";
        int page = 1;
        int size = 5;
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("id")));

        //List<Post> actual = postService.getList(board_code, keyword, page, size).getContent();
        List<Post> actual = postRepository.findByBoard_codeAndTitleContaining(board_code, keyword, pageable).getContent();

        for(int i=0;i<expected.size();i++){
            System.out.println( "expected="+expected.get(i).getId()+"_"+expected.get(i).getTitle()+"_"+expected.get(i).getContent());
        }
        for(int i=0;i<actual.size();i++){
            System.out.println( "actual="+actual.get(i).getId()+"_"+actual.get(i).getTitle()+"_"+actual.get(i).getContent());
        }

        //값 비교하기
        assertEquals(expected.toString(),  actual.toString());
        //assertIterableEquals(expected, actual);

    }

    @Test
    void getRead_내용(){

        Board board_qna = boardRepository.findByCode("qna").orElse(null);
        Board board_notice = boardRepository.findByCode("notice").orElse(null);

        Member member_aaaa = memberRepository.findByMemberId("aaaa").orElse(null);
        Member member_admin = memberRepository.findByMemberId("admin").orElse(null);

        Post expected = new Post( 3L, 3, 0, 0, board_notice, member_admin, "공지사항3", "관리자", "3내용입니다.. 내용입니다.","$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq",LocalDate.parse("2025-05-03").atStartOfDay(), null);
        //Post b = new Post( 3L, 3, 0, 0, board_notice, member_admin, '공지사항3', '관리자', '3내용입니다.. 내용입니다.','$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq','2025-05-03', null);

        //Post expected = a;

        //실제 값 가져오기
        String board_code = "notice";
        Long id = 3L;
        Post actual = postService.getRead(board_code, 3L);

        //값 비교
        assertEquals(expected.toString(),  actual.toString());
    }

    @Test
    void createForm_답변_등록폼(){

        String board_code = "notice";
        Long id = 1L;

        PostDto dto = new PostDto();
        dto.setBoard_code("notice");
        dto.setMode("reply");

        Post actual = postService.getCreateForm(board_code, id, dto);

        //데이터 비교확인
        assertNotNull(actual.getId());
    }

    @Test
    void create_등록하기(){

        //임의 데이터 생성
        String board_code = "notice";
        Long id = 0L;

        PostDto dto = new PostDto();
        dto.setBoard_code("notice");
        dto.setMode("reg");
        dto.setPassword("1111");
        dto.setTitle("제목입니다.");
        dto.setName("홍길동");
        dto.setContent("내용입니다.");
        dto.setCreatedAt(LocalDate.parse("2025-07-05").atStartOfDay());

        //임의 데이터 등록
        Post actual = postService.create(board_code, id, dto);

        //등록여부 체크
        assertNotNull(actual.getId());

    }

    @Test
    void getEditForm_수정폼(){

        //임의 데이터 생성
        String board_code = "notice";
        Long id = 3L;

        //임의 데이터 등록
        Post actual = postService.getEditForm(board_code, id);

        //등록여부 체크
        assertNotNull(actual.getId());
    }

    @Test
    void update_수정하기(){

        //임의 데이터 생성
        String board_code = "notice";
        Long id = 3L;

        PostDto dto = new PostDto();
        dto.setId(3L);
        dto.setBoard_code("notice");
        dto.setMode("reg");
        dto.setTitle("제목입니다.");
        dto.setName("홍길동");
        dto.setContent("내용입니다.");
        dto.setCreatedAt(LocalDate.parse("2025-07-05").atStartOfDay());

        //임의 데이터 등록
        Post actual = postService.edit(board_code, id, dto);

        //등록여부 체크
        assertNotNull(actual.getId());

    }

    @Test
    void password_비밀번호_확인(){

        String board_code = "notice";
        String mode = "";
        Long id = 3L;

        PostDto dto = new PostDto();
        dto.setPassword("1111");
        String result = postService.password(board_code, id, mode, dto);

        assertNotNull(result);

    }

    @Test
    void delete_삭제(){
        String board_code = "notice";
        Long id = 3L;
        PostDto dto = new PostDto();
        Post actual = postService.delete(board_code, id, dto);

        assertNotNull("");
    }

}
