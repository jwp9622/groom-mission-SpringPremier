package com.example.WebServiceBasic.comment.service;

import com.example.WebServiceBasic.domain.boards.service.BoardService;
import com.example.WebServiceBasic.domain.comment.dto.CommentDto;
import com.example.WebServiceBasic.domain.comment.service.CommentService;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.posts.entity.Post;
import com.example.WebServiceBasic.domain.members.repository.MemberRepository;
import com.example.WebServiceBasic.domain.posts.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Autowired
    BoardService boardService;


    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void CommentService_list_일반_목록(){

        //임의 데이터 넣기
        //Comment a = new Comment( 13L, "board10", "board10", "board10 게시판입니다.",10, LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Comment b = new Comment( 12L, "board9", "board9", "board9 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Comment c = new Comment( 11L, "board8", "board8", "board8 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Comment d = new Comment( 10L, "board7", "board7", "board7 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Comment e = new Comment( 9L, "board6", "board6", "board6 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);

        Post post = postRepository.findById(3L).orElse(null);
        Member membera = memberRepository.findByMemberId("aaaa").orElse(null);
        Member memberb = memberRepository.findByMemberId("bbbb").orElse(null);

        CommentDto a = new CommentDto(1L, 3L, "aaaa", "사용자a", "11코멘트 내용입니다... 내용입니다.", "$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq", LocalDate.parse("2025-05-06").atStartOfDay(), null);
        CommentDto b = new CommentDto(2L, 3L, "bbbb", "사용자a", "22코멘트 내용입니다... 내용입니다.","$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq", LocalDate.parse("2025-05-06").atStartOfDay(), null);
        CommentDto c = new CommentDto(3L, 3L, "bbbb", "사용자a", "33코멘트 내용입니다... 내용입니다.","$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq", LocalDate.parse("2025-05-06").atStartOfDay(), null);
        CommentDto d = new CommentDto(4L, 3L, null, "사용자a", "44코멘트 내용입니다... 내용입니다.","$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq", LocalDate.parse("2025-05-06").atStartOfDay(), null);
        CommentDto e = new CommentDto(5L, 3L, null, "사용자a", "55코멘트 내용입니다... 내용입니다.","$2a$10$AL2DGbVtc8yrGEXb39vuvO97V812dI8A.uzMYcO.pdZqU08lcelcq", LocalDate.parse("2025-05-06").atStartOfDay(), null);

        List<CommentDto> expected = Arrays.asList(a,b,c,d,e);

        //실제데이터 5개까지 가져오기
        //String keyword = "";
        //int page = 1;
        //int size = 5;
        //Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("id")));
        List<CommentDto> actual = commentService.getList(3L);

        //값 비교하기
        assertThat(expected).isNotSameAs(actual);

    }

    @Test
    void CommentService_create_등록_성공_여부(){

        Long post_id = 3L;

        //임의 데이터 넣기
        CommentDto dto = new CommentDto();
        dto.setPost_id(3L);
        dto.setMember_memberId("aaaa");
        dto.setPassword("1111");
        dto.setNickname("abcd");
        dto.setContent("코멘트 내용....");
        dto.setCreatedAt(LocalDate.parse("2025-06-23").atStartOfDay());
        dto.setUpdatedAt(null);

        //실제데이터 넣은 결과값
        CommentDto actual = commentService.create(post_id, dto);

        //등록여부 체크
        assertNotNull(actual.getId());
        //assertEquals(expected.toString(),  actual.toString());
    }
    @Test
    void CommentService_create_등록_실패_여부(){

        Long post_id = 31423412343412L;

        //임의 데이터 넣기
        CommentDto dto = new CommentDto();
        dto.setPost_id(3L);
        dto.setMember_memberId("aaaa");
        dto.setPassword("1111");
        dto.setNickname("abcd");
        dto.setContent("코멘트 내용....");
        dto.setCreatedAt(LocalDate.parse("2025-06-23").atStartOfDay());
        dto.setUpdatedAt(null);

        // 예외 발생 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.create(post_id, dto);
        });

        assertEquals("댓글 생성 실패! 대상 게시글이 없습니다.", exception.getMessage());
     }


    @Test
    void CommentService_update_수정_성공_여부(){

        Long post_id = 3L;

        //임의 데이터 넣기
        CommentDto dto = new CommentDto();
        dto.setId(3L);
        dto.setPost_id(3L);
        dto.setMember_memberId("aaaa");
        dto.setPassword("1111");
        dto.setNickname("abcd");
        dto.setContent("코멘트 내용....");
        dto.setCreatedAt(LocalDate.parse("2025-06-23").atStartOfDay());
        dto.setUpdatedAt(null);

        //실제데이터 넣은 결과값
        CommentDto actual = commentService.create(post_id, dto);

        //수정여부 체크
        assertNotNull(actual.getId());
    }

    @Test
    void CommentService_delete_삭제_여부(){

        Long post_id = 3L;

        //임의 데이터 넣기
        CommentDto dto = new CommentDto();
        dto.setId(3L);
        dto.setPost_id(3L);
        dto.setMember_memberId("aaaa");
        dto.setPassword("1111");
        dto.setNickname("abcd");
        dto.setContent("코멘트 내용....");
        dto.setCreatedAt(LocalDate.parse("2025-06-23").atStartOfDay());
        dto.setUpdatedAt(null);

        //실제데이터 넣은 결과값
        CommentDto data = commentService.create(post_id, dto);


        //데이터 삭제
        CommentDto actual = commentService.delete(data.getId());

        //삭제여부 체크
        assertNotNull(actual.getId());
        //assertEquals(expected.toString(),  actual.toString());
    }

}
