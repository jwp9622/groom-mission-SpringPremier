package com.example.SpringPremier.domain.auth.exception;

import com.example.SpringPremier.global.exception.BaseException;
import com.example.SpringPremier.global.exception.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
