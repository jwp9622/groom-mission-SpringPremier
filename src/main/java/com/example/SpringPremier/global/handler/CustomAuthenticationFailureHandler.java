package com.example.SpringPremier.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {


        // 예: 실패 메시지를 쿼리 파라미터로 전달
        String errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";

        // 로그인 실패 시 리다이렉트할 경로 설정
        response.sendRedirect("/login?error=true&message=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
    }
}