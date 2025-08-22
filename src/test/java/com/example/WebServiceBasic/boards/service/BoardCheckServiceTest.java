package com.example.WebServiceBasic.boards.service;

import com.example.WebServiceBasic.domain.boards.entity.Board;
import com.example.WebServiceBasic.domain.boards.service.BoardCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BoardCheckServiceTest {

    @Autowired
    BoardCheckService boardCheckService;

    @Test
    @DisplayName("BoardCheck_게시판 정보 가져오기")
    void test_boardGetInfo(){

        Long id = 1L;

        //List<Board> expected = Arrays.asList(
        //    new Board(id,"notice", "공지사항", "공지사항게시판입니다.",10, LocalDate.parse("2025-06-06").atStartOfDay(), null)
        //);
        //임의 데이터 지정
        Board expected = new Board(id,"notice", "공지사항", "공지사항게시판입니다.",10, LocalDate.parse("2025-06-06").atStartOfDay(),   null);

        //실제 데이터 가져오기
        Board actual = boardCheckService.boardGetInfo("notice");

        //비교하기
        assertEquals(expected.toString(),  actual.toString());
        //assertEquals(expected,  actual);
        //assertThat(expected).isEqualTo(actual);

    }
}
