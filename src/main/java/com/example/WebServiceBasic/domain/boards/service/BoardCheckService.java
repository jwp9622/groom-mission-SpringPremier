package com.example.WebServiceBasic.domain.boards.service;

import com.example.WebServiceBasic.domain.boards.entity.Board;

public interface BoardCheckService {
    //정상적인 접근여부 체크
    String boardCheck(String code);

    //게시판 정보 가져오기
    Board boardGetInfo(String code);

    boolean existsByCode(String code);

    boolean existsByBoard_code(String board_code);
}