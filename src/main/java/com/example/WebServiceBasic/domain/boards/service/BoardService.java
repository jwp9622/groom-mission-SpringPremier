package com.example.WebServiceBasic.domain.boards.service;

import com.example.WebServiceBasic.domain.boards.dto.BoardDto;
import com.example.WebServiceBasic.domain.boards.entity.Board;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
    List<BoardDto> pagenation(Page<Board> list, int total, int page, int size, String keyword);
    Page<BoardDto> getList(String keyword, int page, int size);
    Board getRead(Long id);
    Board create(BoardDto dto);
    Board edit(Long id);
    Board update(BoardDto dto);
    Board delete(Long id);
}
