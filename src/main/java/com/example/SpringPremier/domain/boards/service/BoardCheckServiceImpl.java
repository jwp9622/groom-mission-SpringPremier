package com.example.SpringPremier.domain.boards.service;

import com.example.SpringPremier.domain.boards.entity.Board;
import com.example.SpringPremier.domain.boards.repository.BoardRepository;
import com.example.SpringPremier.domain.posts.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class BoardCheckServiceImpl implements BoardCheckService{

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public String boardCheck(String code){
        Board manager = boardRepository.findByCode(code).orElse(null);
        if(manager == null) return "error/404";
        return "";
    }

    @Override
    public Board boardGetInfo(String code){
        String chk = boardCheck(code);
        if(chk == null) return null;
        return boardRepository.findByCode(code).orElse(null);
    }

    @Override
    public boolean existsByCode(String code) {
        return boardRepository.existsByCode(code);
    }

    @Override
    public boolean existsByBoard_code(String board_code) {
        return postRepository.existsByBoard_code(board_code);
    }

}
