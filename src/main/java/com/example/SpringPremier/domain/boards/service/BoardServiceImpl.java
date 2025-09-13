package com.example.SpringPremier.domain.boards.service;

import com.example.SpringPremier.domain.boards.dto.BoardDto;
import com.example.SpringPremier.domain.boards.entity.Board;
import com.example.SpringPremier.domain.boards.repository.BoardRepository;
import com.example.SpringPremier.domain.posts.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class BoardServiceImpl implements BoardService{

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;


    @Override
    //리스트를 entity 번호 다시 계산
    public List<BoardDto> pagenation( Page<Board> list, int total, int page, int size, String keyword){

        AtomicInteger counter = new AtomicInteger();
        List<BoardDto> dtoList = list.getContent().stream().map(board -> {
            int i = counter.getAndIncrement(); //전체 게시물 수
            Board board2 = (Board) list.getContent().get(i);
            int displayNumber = Math.toIntExact(total - ((long) (page - 1) * size) - i); //계산하여 log ->int 변환
            String param = "keyword="+keyword+"&page="+page;
            return BoardDto.fromEntity(board2, displayNumber,param);
        }).collect(Collectors.toList());



        return dtoList;
    }

    @Override
    //목록
    public Page<BoardDto> getList(String keyword, int page, int size){

        //pageable 선언
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("id")));

        //db 값을 pageable 가져오기
        Page<Board> list;
        if(keyword != null) {
            list = boardRepository.findByNameContaining(keyword, pageable);
        }else{
            list = boardRepository.findAll(pageable);
        }

        //총 개시물수
        int total = Math.toIntExact(list.getTotalElements());

        AtomicInteger counter = new AtomicInteger();

        //게시물 번호 역순으로 번호부여,  new 아이콘 표시
        List<BoardDto> dtoList  = pagenation( list, total, page, size, keyword);


        //dto, pageable, counts 더하기
        Page<BoardDto> dtoPage = new PageImpl<>(dtoList, pageable, list.getTotalElements());

        //일반 목록 - page 리턴
        return dtoPage;

    }

    @Override
    //내용
    public Board getRead(Long id){
        return boardRepository.findById(id).orElse(null);
    }

    @Override
    //등록하기
    public Board create(BoardDto dto){

        Board entity = dto.toEntity();

        //return boardRepository.save(entity);

        try {
            return boardRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Board 저장 실패: " + e.getMessage(), e);
        }


    }


    @Override
    //수정form
    public Board edit(Long id){

        //내용존재여부 체크
        Board target = boardRepository.findById(id).orElse(null);

        if(target == null){
            return null;
        }
        return target;
    }


    @Override
    public Board update(BoardDto dto){

        //내용존재여부 체크
        Board target = boardRepository.findById(dto.getId()).orElse(null);

        if(target != null){

            //엔티티로 넘기기
            Board manager = dto.toEntity();
            Board updated = boardRepository.save(manager);

            return updated;
        }else{
            return null;
        }
    }

    @Override
    public Board delete(Long id){
        //내용존재여부 체크
        Board target = boardRepository.findById(id).orElse(null);

        if(target != null){
            //삭제
            boardRepository.delete(target);
            return target;
        }else{
            return null;
        }
    }
}
