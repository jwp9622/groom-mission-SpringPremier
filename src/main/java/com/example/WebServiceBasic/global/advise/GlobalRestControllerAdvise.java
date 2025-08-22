package com.example.WebServiceBasic.global.advise;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations= RestController.class)
class GlobalRestControllerAdvise   {

    //유효값 체크
    // 400 Bad Request
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> handleCustomValidationException(Exception ex) {
        String message = ex.getMessage();

        Map<String, Object> map2 = new HashMap<>();
        map2.put("success",false);
        map2.put("message",ex.getMessage());
        map2.put("data",null);

        //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(HttpStatus.BAD_REQUEST, message));
        return ResponseEntity.badRequest().body(map2);
    }


    // 404 Not Found
    @ExceptionHandler({
            EntityNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 400 Bad Request / @Valid, @RequestBody 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("유효성 검사 실패");

        return ResponseEntity.badRequest().body(
                ErrorResponse.of(HttpStatus.BAD_REQUEST, message)
        );
    }



    //유효값 체크
    // 400 Bad Request
    @ExceptionHandler({
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {

        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, message));

    }


    // 403 Forbidden (권한 없음)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied2(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."));
    }


    // 409 Conflict (중복 등 데이터 충돌)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(HttpStatus.CONFLICT, "데이터 충돌 오류입니다."));
    }

    // 500 Internal Server Error
    @ExceptionHandler({
            NullPointerException.class,
            RuntimeException.class,
            Exception.class
    })
    public ResponseEntity<ErrorResponse> handleServerError(Exception ex) {
        ex.printStackTrace(); // 로그용
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."));
    }



}
