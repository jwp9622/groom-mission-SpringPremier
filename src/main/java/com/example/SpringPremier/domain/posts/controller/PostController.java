package com.example.SpringPremier.domain.posts.controller;

import com.example.SpringPremier.domain.boards.entity.Board;
import com.example.SpringPremier.domain.comment.dto.CommentDto;
import com.example.SpringPremier.domain.comment.service.CommentService;
import com.example.SpringPremier.domain.posts.dto.PageInfoDto;
import com.example.SpringPremier.domain.boards.service.BoardCheckServiceImpl;
import com.example.SpringPremier.domain.boards.service.BoardServiceImpl;
import com.example.SpringPremier.domain.posts.dto.PostDto;
import com.example.SpringPremier.domain.posts.entity.Post;
import com.example.SpringPremier.domain.posts.error.PostErrorCode;
import com.example.SpringPremier.domain.posts.service.PostService;
import com.example.SpringPremier.global.exception.ValidateHandling;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private BoardServiceImpl boardService;

    @Autowired
    private BoardCheckServiceImpl boardCheckService;

    @Autowired
    private CommentService commentService;


    @Autowired
    private ValidateHandling validateHandling;

    //접근 가능여부 체크 메소드
    private Board board;


    @GetMapping("/posts/{board_code}")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @PathVariable String board_code, Model model){

        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }


        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        // Page 게시물 표시
        // 페이지 처리할때에는 1부터 시작, service에서 -1한다.
        Page<PostDto> postPage = postService.getList(board_code, keyword,  page, pageSize);

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
        model.addAttribute("board_name", board.getName());
        model.addAttribute("board_code", board_code != null ? board_code:""); //키워드
        model.addAttribute("keyword", keyword != null ? keyword:""); //키워드
        model.addAttribute("posts", postPage.getContent()); //내용 목록

        model.addAttribute("pageNumbers", pageNumbers); //페이지징 배열
        model.addAttribute("blockPage", blockPage); //현재페이지 번호
        model.addAttribute("currentPage", currentPage); //현재페이지 번호
        model.addAttribute("totalPages", totalPages); //전체 페이지수
        model.addAttribute("totalCounts", totalCounts); //전체 게시물수
        model.addAttribute("prevBlockPage", prevBlockPage > 0 ? prevBlockPage : null); //이전블럭 블록여부
        model.addAttribute("nextBlockPage", nextBlockPage <= totalPages ? nextBlockPage : null); //다음블럭 여부

        //4. 뷰 페이지 설정하기
        return "posts/list";
    }


    @GetMapping("/posts/{board_code}/{id}")
    public String read(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String page,
                       @PathVariable String board_code,
                       @PathVariable Long id,
                       Model model){

        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }

        //글내용
        Post post = postService.getRead(board_code, id);

        //기존 글 존재여부 체크
        if(post ==  null){
            return "redirect:/";
        }


        //코멘트 가져오기
        List<CommentDto> commentsDtos = commentService.getList(id);

        //3. 모델에 데이터 등록하기.
        model.addAttribute("board_name", board.getName());
        model.addAttribute("board_code", board_code != null ? board_code:""); //키워드
        model.addAttribute("keyword", keyword != null ? keyword:""); //키워드
        model.addAttribute("page", page != null ? page:""); //키워드
        model.addAttribute("post", post);
        model.addAttribute("commentDtos", commentsDtos); // 댓글 목록 모델에 등록

        //4. 내용보기로 설정
        return "posts/read";
    }

    @GetMapping("/posts/{board_code}/new")
    public String createForm(@PathVariable String board_code,
                             PostDto dto, Model model){

        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }

        //서비스 글등록 호출
        dto.setMode("create");
        Post post = postService.getCreateForm(board_code, 0L, dto);

        //모델로 넘기기
        model.addAttribute("board_name", board.getName());
        model.addAttribute("board_code", board_code != null ? board_code:""); //키워드

        //2. 등록 페이지 설정
        return "posts/new";
    }

    @PostMapping("/posts/{board_code}/new")
    public String create(@PathVariable String board_code,
                         @Valid PostDto dto, Errors errors, Model model){

        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }

        //등록 양식 중복체크
        if (errors.hasErrors()) {

            // 회원가입 실패시 입력 데이터 값을 유지
            model.addAttribute("post",dto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling.validateHandling("invalid",errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            // 회원가입 페이지로 다시 리턴
            return "posts/new";
        }

        dto.setMode("create");

        //글 등록 가져오기
        Post post = postService.create(board_code, 0L, dto);

       //4.목록으로 이동
        return "redirect:/posts/"+board_code;
    }


    @GetMapping("/posts/{board_code}/{id}/reply")
    public String replyForm(@PathVariable String board_code,
                            @PathVariable Long id,
                            PostDto dto, Errors errors, Model model){


        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            errors.rejectValue("board_code","코드", PostErrorCode.BOARD_CODE_NOT_EXISTS.getMessage()); //"정상적인 값이 아닙니다."
        }

        //답변선언
        dto.setMode("reply");

        //서비스 글등록 호출
        Post parent_post = postService.getCreateForm(board_code, id, dto);

        //model 전달
        model.addAttribute("board_name", board.getName());
        model.addAttribute("board_code", board_code != null ? board_code:""); //키워드
        model.addAttribute("parent_post", parent_post);

        //2. 등록 페이지 설정
        return "posts/reply";
    }

    @PostMapping("/posts/{board_code}/{id}/reply")
    public String reply(@PathVariable String board_code,
                        @PathVariable Long id,
                        @Valid PostDto dto, Errors errors, Model model    ){

        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }

        //등록 양식 체크
        if (errors.hasErrors()) {


            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling.validateHandling("valid",errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            // 회원가입 페이지로 다시 리턴
            return "posts/reply";
        }


        //답변선언
        dto.setMode("reply");

        //글 등록 가져오기
        Post post = postService.create(board_code, id, dto);

        //4.목록으로 이동
        //return "";
        return "redirect:/posts/"+board_code;

    }


    @GetMapping("/posts/{board_code}/{id}/edit")
    public String editForm(@RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String page,
                           @PathVariable String board_code,
                           @PathVariable Long id,
                           Model model){


        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);


        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }

        //게시물 가져오기
        Post post = postService.getEditForm(board_code, id);

        //3. 모델에 넘겨주기
        model.addAttribute("board_name", board.getName());
        model.addAttribute("board_code", board_code != null ? board_code:""); //키워드
        model.addAttribute("keyword", keyword != null ? keyword:""); //키워드
        model.addAttribute("page", page != null ? page:""); //키워드
        model.addAttribute("post", post);

        //4. 수정 페이지 이동
        return "/posts/edit";

    }

    @PostMapping("/posts/{board_code}/{id}/edit")
    public String edit(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) String page,
                         @PathVariable String board_code,
                         @PathVariable Long id,
                         @Valid PostDto dto, Errors errors, Model model){



        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            errors.rejectValue("board_code","코드", PostErrorCode.BOARD_CODE_NOT_EXISTS.getMessage()); //"정상적인 값이 아닙니다."
        }

        //등록 양식 체크
        if (errors.hasErrors()) {

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling.validateHandling("valid",errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            // 회원가입 페이지로 다시 리턴
            return "/posts/{board_code}/new";
        }



        //내용 저장
        Post post = postService.edit(board_code, id, dto);

        //5. 상세내용 페이지 이동
        return "redirect:/posts/"+board_code+"/"+id;
    }

    @GetMapping(value = {"/posts/{board_code}/{id}/{mode}/password"})
    public String passwordForm(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String page,
                                @PathVariable String board_code,
                                @PathVariable Long id,
                                @PathVariable String mode,
                                PostDto dto,
                                Model model){

        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }

        model.addAttribute("board_name", board.getName());
        model.addAttribute("board_code", board_code != null ? board_code:""); //키워드

        //2.비밀번호 페이지 이동
        return "/posts/password";
    }


    @PostMapping(value = {"/posts/{board_code}/{id}/{mode}/password"})
    public String password(@RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String page,
                           @PathVariable String board_code,
                           @PathVariable Long id,
                           @PathVariable String mode,
                           PostDto dto, Model model, RedirectAttributes rttr){

        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }


        //비밀번호 확인
        String retValue = postService.password(board_code, id, mode, dto);

        //url 설정
        String retUrl;
        if(retValue.equals("no_post")) {
            rttr.addFlashAttribute("msg",PostErrorCode.DATA_NOT_EXISTS.getMessage());   //"등록된 글이 없습니다."
            retUrl = "/posts/"+board_code+"/"+id+"/"+mode+"/password";
        }else if(retValue.equals("not_equal_password")){
            rttr.addFlashAttribute("msg",PostErrorCode.PASSWORD_NOT_MISMATCH.getMessage());   //"비밀번호가 일치하지 않습니다."
            retUrl = "/posts/"+board_code+"/"+id+"/"+mode+"/password";
        }else{
            if(mode.equals("edit")){
                retUrl = "/posts/" + board_code+"/"+id+"/edit";
            }else{
                rttr.addFlashAttribute("msg", PostErrorCode.DELETED.getMessage());  //"삭제되었습니다."
                retUrl = "/posts/" + board_code;
            }
        }

        System.out.println("retValue="+retValue);
        System.out.println("board_code="+board_code);
        System.out.println("id="+id);
        System.out.println("mode="+mode);
        System.out.println("retUrl="+retUrl);


        //3.리스트 페이지 이동
        return "redirect:"+retUrl;

    }



    @PostMapping("/posts/{board_code}/{id}/delete")
    public String delete(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) String page,
                         @PathVariable String board_code,
                         @PathVariable Long id,PostDto dto, RedirectAttributes rttr){

        //게시판 제목 가져오기
        board = boardCheckService.boardGetInfo(board_code);

        //기존 게시판 존재여부 체크
        if(board ==  null){
            return "redirect:/";
        }

        Post post = postService.delete(board_code, id, dto);

        //url 설정
        String retUrl;
        if(post ==null ){
            rttr.addFlashAttribute("msg", PostErrorCode.PASSWORD_NOT_MISMATCH.getMessage());   //"비밀번호가 일치하지 않습니다."
            retUrl = "/posts/"+board_code+"/password";
        }else {
            //postRepository.delete(post);
            rttr.addFlashAttribute("msg", PostErrorCode.DELETED.getMessage());  //"삭제되었습니다."
            retUrl = "/posts/" + board_code ;
        }

        //3.리스트 페이지 이동
        return "redirect:"+retUrl;

    }

    @PostMapping("/posts/transaction-test")
    public ResponseEntity<List<Post>> transactionTest(@RequestBody
                                                         List<PostDto> dtos) {
        List<Post> createdList = postService.createBoards(dtos);
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
