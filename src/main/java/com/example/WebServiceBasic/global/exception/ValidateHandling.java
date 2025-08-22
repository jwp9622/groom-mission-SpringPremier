package com.example.WebServiceBasic.global.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

public class ValidateHandling {

    /* 회원가입 시, 유효성 체크 */
    public Map<String, String> validateHandling(String preTag, Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format( preTag +"_%s", error.getField());

            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }


}
