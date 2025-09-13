package com.example.SpringPremier.domain.comment.controller;

import com.example.SpringPremier.domain.comment.dto.CommentDto;
import com.example.SpringPremier.domain.comment.service.CommentServiceImpl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentApiController {

    @Autowired
    private CommentServiceImpl boardCommentService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //목록
    @GetMapping("/api/posts/{post_id}/comments")
    public ResponseEntity<List<CommentDto> >list(@PathVariable Long post_id){

        List<CommentDto> boardCommentDto = boardCommentService.getList(post_id);


        return ResponseEntity.status(HttpStatus.OK).body(boardCommentDto);
    }
    
    //등록
    @PostMapping("/api/posts/{post_id}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long post_id,
                                             @RequestBody @Valid CommentDto dto,
                                             BindingResult bindingResult){

        // 유효성 검사 결과가 오류일 경우
        if (bindingResult.hasErrors()) {

            // 유효성 오류 메시지 목록을 만들기
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("유효성 검사 오류");

            System.out.println( errorMessage );

            // 400 Bad Request와 함께 오류 메시지 반환
            return ResponseEntity.badRequest().body(null);
        }

        CommentDto boardCommentDto = boardCommentService.create(post_id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(boardCommentDto);


    }
    
    //수정
    @PatchMapping("/api/posts/comments/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id,
                                             @RequestBody @Valid CommentDto dto,
                                             BindingResult bindingResult){
        // 유효성 검사 결과가 오류일 경우
        if (bindingResult.hasErrors()) {

            // 유효성 오류 메시지 목록을 만들기
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("유효성 검사 오류");

            System.out.println( errorMessage );

            // 400 Bad Request와 함께 오류 메시지 반환
            return ResponseEntity.badRequest().body(null);
        }
            CommentDto boardCommentDto = boardCommentService.update(id, dto);

            return ResponseEntity.status(HttpStatus.OK).body(boardCommentDto);
    }

    //삭제
    @DeleteMapping("/api/posts/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id){

        CommentDto boardCommentDto = boardCommentService.delete(id);

        if (boardCommentDto == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(boardCommentDto);
    }

    //비밀번호 확인
    @PostMapping("/api/posts/comments/passwordCheck/{id}")
    public ResponseEntity<CommentDto> checkPassword(@PathVariable Long id,
                                                    @RequestBody CommentDto dto){

        boolean isMatch = boardCommentService.passwordCheck(dto);

        if(!isMatch){   //비밀번호가 일치하지 않으면

            //결과내용 보내기
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);

        }else{  //비밀번호가 일치하면

            //결과내용 보내기
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        }
    }





}
