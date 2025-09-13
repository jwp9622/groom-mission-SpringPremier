package com.example.SpringPremier.domain.posts.error;

import com.example.SpringPremier.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements ErrorCode {

    BOARD_CODE_NOT_EXISTS(HttpStatus.CONFLICT, "정상적인 값이 아닙니다."),
    DATA_NOT_EXISTS(HttpStatus.CONFLICT, "등록된 글이 없습니다."),
    PASSWORD_NOT_MISMATCH(HttpStatus.CONFLICT, "비밀번호가 일치하지 않습니다."),
    DELETED(HttpStatus.CONFLICT, "삭제되었습니다.");


    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }



}
