package com.example.WebServiceBasic.domain.user.error;

import com.example.WebServiceBasic.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

  ID_PASSWORD_NOT_MISMATCH(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다."),
  ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 아이디입니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
  PASSWORD_NOT_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

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
