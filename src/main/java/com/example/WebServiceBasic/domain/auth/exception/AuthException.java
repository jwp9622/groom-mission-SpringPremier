package com.example.WebServiceBasic.domain.auth.exception;

import com.example.WebServiceBasic.global.exception.BaseException;
import com.example.WebServiceBasic.global.exception.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
