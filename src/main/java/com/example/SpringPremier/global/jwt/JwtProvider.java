package com.example.SpringPremier.global.jwt;

import com.example.SpringPremier.domain.auth.dto.AuthRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@Getter
public class JwtProvider {

    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${spring.jwt.secret}") String secretKey,
            @Value("${spring.jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${spring.jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    //Access Token 생성
    public String createAccessToken(AuthRequest request) {
        //return createToken(request, accessTokenValidity);
        return Jwts.builder()
                .setSubject(request.getUsername())
                .claim("username", request.getUsername2())
                .claim("role", request.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //RefreshToken 생성
    public String createRefreshToken(AuthRequest request) {
        //return createToken(request, refreshTokenValidity);

        return Jwts.builder()
                .setSubject(request.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public long getExpiration(String token) {
        Claims claims = parseClaims(token);
        return (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000;
    }



    // Request 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveCookieToken(HttpServletRequest request) {

        // 쿠키에서 토큰 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("    "+cookie.getName()+"====>"+cookie.getValue());

                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    //토큰에서 role 추출 (AccessToken일 때만 사용 가능)
    public String getUsernameFromToken(String token) {
        return (String) parseClaims(token).getSubject();
    }

   //JWT 검증
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않은 JWT입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 요청 문자열이 비어있습니다.", e);
        }
        return false;
    }

    //JWT Claims 추출
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}

