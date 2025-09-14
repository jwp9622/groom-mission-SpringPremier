package com.example.SpringPremier.domain.posts.controller;


import com.example.SpringPremier.domain.members.service.MemberServiceImpl;
import com.example.SpringPremier.domain.posts.dto.PageResponseDto;
import com.example.SpringPremier.domain.posts.dto.PostDto;
import com.example.SpringPremier.domain.posts.entity.Post;
import com.example.SpringPremier.domain.posts.service.PostService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class PostApiController {

    @Autowired
    PostService postService;

    @Autowired
    MemberServiceImpl memberService;

    //목록 get
    @GetMapping("/api/posts/{board_code}")
    public ResponseEntity<PageResponseDto<PostDto>> list(@PathVariable String board_code,
                                                          @RequestParam(required = false) String keyword,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          Model model){

        Page<PostDto> postPage = postService.getList(board_code, keyword,  page, pageSize);

        PageResponseDto<PostDto> responseDto = new PageResponseDto<>(postPage, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

    //등록 post
    @PostMapping("/api/posts/{board_code}")
    public ResponseEntity<Post> create(@RequestBody @Valid PostDto dto, BindingResult bindingResult){

        // 유효성 검사 결과가 오류일 경우
        if (bindingResult.hasErrors()) {
            // 유효성 오류 메시지 목록을 만들기
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("유효성 검사 오류");

            // 400 Bad Request와 함께 오류 메시지 반환
            return ResponseEntity.badRequest().body(null);
        }

        dto.setMode("create");
        Post created = postService.create( dto.getBoard_code(), 0L, dto);

        return (created != null)?
                ResponseEntity.status(HttpStatus.OK).body(created):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //수정patch
    @PatchMapping("/api/posts/{board_code}/{id}")
    public ResponseEntity<Post> update(@PathVariable String board_code,
                                       @PathVariable Long id,
                                       @RequestBody @Valid PostDto dto, BindingResult bindingResult){

        // 유효성 검사 결과가 오류일 경우
        if (bindingResult.hasErrors()) {
            // 유효성 오류 메시지 목록을 만들기
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("유효성 검사 오류");

            // 400 Bad Request와 함께 오류 메시지 반환
            return ResponseEntity.badRequest().body(null);
        }

        Post post = postService.edit(board_code, id, dto);
        return (post != null)?
                ResponseEntity.status(HttpStatus.OK).body(post):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //삭제delete
    @Transactional
    @DeleteMapping("/api/posts/{board_code}/{id}")
    public ResponseEntity<Post> delete(@PathVariable String board_code, @PathVariable Long id, PostDto dto){


        //password 문제로 링크로 넘기면 삭제 안됨. 비밀번호 있어야 된다.
        Post post = postService.delete(board_code, id, dto);
        return (post != null)?
                ResponseEntity.status(HttpStatus.OK).body(post):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}
