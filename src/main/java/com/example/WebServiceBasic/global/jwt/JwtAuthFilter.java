package com.example.WebServiceBasic.global.jwt;

import com.example.WebServiceBasic.domain.auth.role.Role;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.global.adapter.CustomUserDetails;
import com.example.WebServiceBasic.global.adapter.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProvider jwtProvider;

    //JWT 검증 필터 수행
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {


        //String authorizationHeader = request.getHeader("Authorization");
        String token = resolveToken(request);

        //JWT 헤더가 있을 경우
        //if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        if(token != null && jwtProvider.isValidToken(token)) {

            //String token = authorizationHeader.substring(7);

            //JWT 유효성 검증
            if (jwtProvider.isValidToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);

                //유저와 토큰 일치 시 userDetails 생성
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username.toString());

                if (userDetails != null) {

                    //UserDetails, Password, Role -> 접근 권한 인증 Token 생성
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    //세션에 사용자 등록
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response); //다음 필터로 넘김
    }

    private String resolveToken(HttpServletRequest request) {
        // 쿠키에서 토큰 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }


}