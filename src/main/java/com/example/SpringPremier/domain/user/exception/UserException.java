package com.example.SpringPremier.domain.user.exception;

import com.example.SpringPremier.global.exception.BaseException;
import com.example.SpringPremier.global.exception.ErrorCode;

public class UserException extends BaseException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
