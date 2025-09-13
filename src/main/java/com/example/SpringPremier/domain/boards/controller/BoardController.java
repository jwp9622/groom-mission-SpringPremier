package com.example.SpringPremier.domain.boards.controller;

import com.example.SpringPremier.domain.boards.dto.BoardDto;
import com.example.SpringPremier.domain.boards.entity.Board;
import com.example.SpringPremier.domain.boards.error.BoardErrorCode;
import com.example.SpringPremier.domain.boards.service.BoardCheckService;
import com.example.SpringPremier.domain.boards.service.BoardService;
import com.example.SpringPremier.domain.posts.dto.PageInfoDto;
import com.example.SpringPremier.global.exception.ValidateHandling;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardCheckService boardCheckService;

    @Autowired
    private ValidateHandling validateHandling;

    @GetMapping("/boards")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize,
                       Model model){


        // Page 게시물 표시
        // 페이지 처리할때에는 1부터 시작, service에서 -1한다.
        Page<BoardDto> postPage = boardService.getList(keyword,  page, pageSize);


        //페이징 처리
        int offset = (page - 1) * pageSize;
        int currentPage = postPage.getNumber()+1; //현재페이지
        int totalPages= postPage.getTotalPages(); //총페이지수
        long totalCounts = postPage.getTotalElements(); //전체 개시물수

        int blockPage = 5; //페이지 나타나는 갯수
        int startPage = ((currentPage-1)/blockPage)*blockPage+1; //블럭 첫페이지
        int endPage = Math.min(startPage + blockPage - 1, totalPages);; //블럭 마지막 페이지

        //페이지 숫자처리(1,2,3,4,5)
        List<PageInfoDto> pageNumbers = IntStream.range(startPage, endPage +1)
                .asLongStream().mapToObj(p -> {
                    return new PageInfoDto(p, p == currentPage); })
                .collect(Collectors.toList());

        int prevBlockPage = startPage - 1;
        int nextBlockPage = endPage + 1;

        //모델로 넘기기
        model.addAttribute("keyword", keyword != null ? keyword:""); //키워드
        model.addAttribute("boards", postPage.getContent()); //내용 목록
        model.addAttribute("pageNumbers", pageNumbers); //페이지징 배열
        model.addAttribute("blockPage", blockPage); //현재페이지 번호
        model.addAttribute("currentPage", currentPage); //현재페이지 번호
        model.addAttribute("totalPages", totalPages); //전체 페이지수
        model.addAttribute("totalCounts", totalCounts); //전체 게시물수
        model.addAttribute("prevBlockPage", prevBlockPage > 0 ? prevBlockPage : null); //이전블럭 블록여부
        model.addAttribute("nextBlockPage", nextBlockPage <= totalPages ? nextBlockPage : null); //다음블럭 여부

        //4. 뷰 페이지 설정하기
        return "admin/boards/list";

    }

    @GetMapping("/boards/{id}")
    public String read(@PathVariable Long id, Model model){

        //게시판 제목 가져오기 체크
        Board board = boardService.getRead(id);

        model.addAttribute("board", board);

        //4. 뷰 페이지 설정하기
        return "admin/boards/read";
    }

    @GetMapping("/boards/new")
    public String createFrom(Model model){

        //4. 뷰 페이지 설정하기
        return "admin/boards/new";
    }

    @PostMapping("/boards/new")
    public String create(@Valid BoardDto dto, Errors errors, Model model){

        //코드명 중복체크
        boolean exists = boardCheckService.existsByCode(dto.getCode());
        if(exists){
            errors.rejectValue("code","코드중복", BoardErrorCode.CODE_ALREADY_EXISTS.getMessage());
        }

        //등록 양식 중복체크
        if (errors.hasErrors()) {

            // 회원가입 실패시 입력 데이터 값을 유지
            model.addAttribute("board",dto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling.validateHandling("invalid",errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            // 회원가입 페이지로 다시 리턴
            return "admin/boards/new";
        }

        Board board = boardService.create(dto);

        //4. 뷰 페이지 설정하기
        return "redirect:/admin/boards";
    }


    @GetMapping("/boards/{id}/edit")
    public String editFrom(@PathVariable Long id, @RequestParam(required = false) String keyword, Model model){

        Board board = boardService.edit(id);

        model.addAttribute("board", board);

        //4. 뷰 페이지 설정하기
        return "admin/boards/edit";
    }

    @PostMapping("/boards/{id}/edit")
    public String edit(@Valid BoardDto dto, Errors errors, Model model,
                       @RequestParam(required = false) String keyword){

        //등록 양식 중복체크
        if (errors.hasErrors()) {

            //게시판 제목 가져오기 체크
            Board board = boardService.getRead(dto.getId());

            // 실패시 입력 데이터 값을 유지
            model.addAttribute("board", board);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling.validateHandling("invalid",errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));

                System.out.println(key+"___"+validatorResult.get(key));
            }

            // 수정 페이지로 다시 리턴
            return "admin/boards/edit";
        }

        Board board = boardService.update(dto);

        model.addAttribute("board", board);

        //뷰 페이지 설정하기
        return "redirect:/admin/boards/"+dto.getId();
    }


    @GetMapping("/boards/{id}/delete")
    public String deleteForm(@PathVariable Long id, @RequestParam(required = false) String keyword, Model model){

        Board board = boardService.edit(id);

        model.addAttribute("board", board);

        //뷰 페이지 설정하기
        return "admin/boards/delete";

    }

    @PostMapping("/boards/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam(required = false) String keyword, Model model){

        //게시판 제목 가져오기 체크
        Board board_db = boardService.getRead(id);

        //post 에서 등록된 게시물 있는지 확인
        boolean existsBoardCode = boardCheckService.existsByBoard_code(board_db.getCode());

        if(existsBoardCode) {

            //기존 과정 가져오기
            Board board = boardService.edit(id);

            //model로 넘기기
            model.addAttribute("board", board);

            model.addAttribute("invalid_data", BoardErrorCode.POSTS_ALREADY_EXISTS.getMessage());

            // 내용 페이지로 다시 리턴
            return "admin/boards/delete";

        }
        Board board = boardService.delete(id);

        //뷰 페이지 설정하기
        return "redirect:/admin/boards";

    }



}
