package com.example.WebServiceBasic.global.advise;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
        String error,
        String message,
        int status,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(
                status.getReasonPhrase(),
                message,
                status.value(),
                LocalDateTime.now()
        );
    }
}
