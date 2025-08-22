package com.example.WebServiceBasic.domain.members.error;

import com.example.WebServiceBasic.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {

  ROLE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "권한정보가 없습니다.");

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
