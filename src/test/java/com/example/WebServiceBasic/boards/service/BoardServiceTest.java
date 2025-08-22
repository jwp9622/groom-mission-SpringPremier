package com.example.WebServiceBasic.boards.service;

import com.example.WebServiceBasic.domain.boards.dto.BoardDto;
import com.example.WebServiceBasic.domain.boards.entity.Board;
import com.example.WebServiceBasic.domain.boards.repository.BoardRepository;
import com.example.WebServiceBasic.domain.boards.service.BoardServiceImpl;
import com.example.WebServiceBasic.domain.members.repository.MemberRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    BoardServiceImpl boardService;

    @Autowired
    BoardRepository boardRepository;

    @Test
    void BoardService_list_일반_목록(){

        //임의 데이터 넣기
        Board a = new Board( 13L, "board10", "board10", "board10 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        Board b = new Board( 12L, "board9", "board9", "board9 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        Board c = new Board( 11L, "board8", "board8", "board8 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        Board d = new Board( 10L, "board7", "board7", "board7 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        Board e = new Board( 9L, "board6", "board6", "board6 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);

        List<Board> expected = Arrays.asList(a,b,c,d,e);

        //실제데이터 5개까지 가져오기
        String keyword = "";
        int page = 1;
        int size = 5;
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("id")));
        //List<Board> actual = boardService.getList(keyword, page, size, pageable).getContent();
        List<Board> actual = boardRepository.findByNameContaining(keyword, pageable).getContent();

        //값 비교하기
        assertEquals(expected.toString(),  actual.toString());

    }

    @Test
    void BoardService__list_검색_목록(){

        //임의 데이터 넣기
        Board a = new Board( 13L, "board10", "board10", "board10 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Board b = new Board( 12L, "board9", "board9", "board9 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Board c = new Board( 11L, "board8", "board8", "board8 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Board d = new Board( 10L, "board7", "board7", "board7 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Board e = new Board( 9L, "board6", "board6", "board6 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);

        List<Board> expected = Arrays.asList(a);

        //실제데이터 5개까지 가져오기
        String keyword = "board10";
        int page = 1;
        int size = 5;
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("id")));
        //List<Board> actual = boardService.getListList(keyword, page, size, pageable).getContent();
        List<Board> actual = boardRepository.findByNameContaining(keyword, pageable).getContent();

        //값 비교하기
        assertEquals(expected.toString(),  actual.toString());

    }


    @Test
    void BoardService_read_상세_내용(){

        //임의 데이터 넣기
        Board a = new Board( 13L, "board10", "board10", "board10 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Board b = new Board( 12L, "board9", "board9", "board9 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Board c = new Board( 11L, "board8", "board8", "board8 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Board d = new Board( 10L, "board7", "board7", "board7 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);
        //Board e = new Board( 9L, "board6", "board6", "board6 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);

        Board expected = a;

        //실제데이터 가져오기
        Board actual = boardService.getRead(13L);

        //값 비교하기
        assertEquals(expected.toString(),  actual.toString());

    }

    @Test
    void BoardService_create_등록_성공_여부(){

        //임의 데이터 넣기
        BoardDto dto = new BoardDto();
        dto.setCode("abcd");
        dto.setName("게시판");
        dto.setDescription("게시판입니다.");
        dto.setPageSize(10);

        //실제데이터 넣은 결과값
        Board actual = boardService.create(dto);

        //등록여부 체크
        assertNotNull(actual.getId());
        //assertEquals(expected.toString(),  actual.toString());
    }
    @Test
    void BoardService_create_등록_실패_여부(){

        //임의 데이터 넣기
        BoardDto dto = new BoardDto();
        dto.setCode("board10");
        dto.setName("게시판");
        dto.setDescription("게시판입니다.");
        dto.setPageSize(10);

        //데이터 넣기
        //System.out.println("=============="+actual.getId());
        assertThrows(IllegalStateException.class, () ->{
            Board actual = boardService.create(dto);

            //등록안되었는지 여부 체크, 중복 코드는 등록되면 안됨
            assertNull(actual);
        });

    }   
    
    

    @Test
    void BoardService_edit_수정폼_등록_여부(){

        //임의 데이터 넣기
        Board expected = new Board( 13L, "board10", "board10", "board10 게시판입니다.",10,LocalDate.parse("2025-06-05").atStartOfDay(), null);

        //실제데이터 넣은 결과값
        Board actual = boardService.edit(13L);

        //등록여부 체크
        assertEquals(expected.toString(),  actual.toString());
    }


    @Test
    void BoardService_update_수정_성공_여부(){

        //임의 데이터 넣기
        BoardDto dto = new BoardDto();
        dto.setId(10L);
        dto.setName("board10");
        dto.setDescription("board10 게시판입니다.");
        dto.setPageSize(10);

        //실제데이터 넣은 결과값
        Board actual = boardService.create(dto);

        //등록여부 체크
        assertNotNull(actual.getId());
        //assertEquals(expected.toString(),  actual.toString());
    }

    @Test
    void BoardService_delete_삭제_여부(){

        //임의 데이터 넣기
        BoardDto dto = new BoardDto();
        dto.setCode("abcd");
        dto.setName("abcd");
        dto.setDescription("abcd 게시판입니다.");
        dto.setPageSize(10);

        //데이터 등록
        Board data = boardService.create(dto);

        //데이터 삭제
        Board actual = boardService.delete(data.getId());

        //삭제여부 체크
        assertNotNull(actual.getId());
        //assertEquals(expected.toString(),  actual.toString());
    }
}

/*
assertEquals(expected, actual);      // 값이 같은지
assertNotEquals(unexpected, actual); // 값이 다른지
assertTrue(condition);               // 조건이 true 인지
assertFalse(condition);              // 조건이 false 인지
assertNull(object);                  // null 여부
assertNotNull(object);               // not null 여부
assertThrows(Exception.class, () -> {
        // 예외 발생하는 코드
        });
assertAll(                          // 여러 개를 한 번에 검사
    () -> assertEquals(...),
    () -> assertTrue(...)
);

assertThat(memberRepository).isInstanceOf(MemberRepository.class);

        assertThrows(NoSuchBeanDefinitionException.class,
                () ->ac.getBean("xxx",MemberService.class));
assertThat(discount).isEqualTo(1000);
assertThat(protoTypeBean1).isNotSameAs(protoTypeBean2);
ssertThat(memberService.getMemberRepository()).isSameAs(memberRepository);

*/
