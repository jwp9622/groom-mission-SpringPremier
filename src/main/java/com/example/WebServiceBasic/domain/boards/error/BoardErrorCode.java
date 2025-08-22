package com.example.WebServiceBasic.domain.boards.error;

import com.example.WebServiceBasic.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardErrorCode implements ErrorCode {

  CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 코드명입니다."),
  POSTS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 게시물이 있으므로 삭제불가능합니다.");

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
