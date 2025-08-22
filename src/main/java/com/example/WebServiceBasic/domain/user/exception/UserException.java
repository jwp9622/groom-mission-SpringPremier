package com.example.WebServiceBasic.domain.user.exception;

import com.example.WebServiceBasic.global.exception.BaseException;
import com.example.WebServiceBasic.global.exception.ErrorCode;

public class UserException extends BaseException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
