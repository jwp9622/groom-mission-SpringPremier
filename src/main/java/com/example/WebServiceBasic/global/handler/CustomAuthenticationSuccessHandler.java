package com.example.WebServiceBasic.global.handler;

import com.example.WebServiceBasic.global.adapter.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {


        // 커스텀 UserDetails를 사용하는 경우
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        HttpSession session = request.getSession();


        // 세션에 사용자 정보 저장
        session.setMaxInactiveInterval(60 * 30);
        String role = userDetails.getAuthorities().toString();
        session.setAttribute("username", userDetails.getUsername());
        session.setAttribute("username2", userDetails.getUsername2());
        session.setAttribute("role", userDetails.getRole());

        response.sendRedirect("/");

    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication authentication)
            throws IOException, ServletException {


    }


    // 로그아웃 성공 시 세션 무효화
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("/login?logout");
        };
    }


}
