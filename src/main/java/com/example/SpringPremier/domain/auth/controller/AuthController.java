package com.example.SpringPremier.domain.auth.controller;

import com.example.SpringPremier.domain.auth.dto.AuthRequest;
import com.example.SpringPremier.domain.auth.dto.AuthResponse;
import com.example.SpringPremier.domain.auth.dto.UserInfoDto;
import com.example.SpringPremier.domain.auth.error.AuthErrorCode;
import com.example.SpringPremier.domain.auth.role.Role;
import com.example.SpringPremier.domain.auth.service.AuthService;
import com.example.SpringPremier.domain.members.dto.MemberDto;
import com.example.SpringPremier.domain.members.service.MemberItemCheck;
import com.example.SpringPremier.global.adapter.CustomUserDetails;
import com.example.SpringPremier.global.exception.ValidateHandling;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private MemberItemCheck memberItemCheck;

    @Autowired
    private ValidateHandling validateHandling;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody MemberDto dto, Errors errors, Model model) {

        //아이디 중복체크
        boolean exists = memberItemCheck.checkUserId(dto.getMemberId());
        if(exists){
            errors.rejectValue("memberId","아이디중복", AuthErrorCode.ID_ALREADY_EXISTS.getMessage());
        }

        //이메일 중복확인
        boolean exists_email = memberItemCheck.checkEmail(dto.getEmail());
        if(exists_email){
            errors.rejectValue("email","이메일중복", AuthErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
        }

        if(!dto.getPassword().equals(dto.getConfirm_password())){
            errors.rejectValue("confirm_password", "비밀번호 일치 오류", AuthErrorCode.PASSWORD_NOT_MISMATCH.getMessage());
        }

        //등록 양식 중복체크
        if (errors.hasErrors()) {

            // 회원가입 실패시 입력 데이터 값을 유지
            model.addAttribute("member",dto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling.validateHandling("invalid",errors);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("success",false);
            map2.put("message","등록이 실패하였습니다.");
            map2.put("data",validatorResult);

            // 회원가입 페이지로 다시 리턴
            return ResponseEntity.badRequest().body(map2);
        }

        //권한 할당
        dto.setRole(String.valueOf(Role.ROLE_USER));

        //회원등록
        authService.register(dto);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("success",true);
        map2.put("message","성공적으로 등록되었습니다.");
        map2.put("data",null);

        return ResponseEntity.ok().body(map2);
        //return ResponseEntity.ok(model);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {

        //로그인 체크후 토큰값 반환
        AuthResponse authResponse =  authService.login(request);


        // JWT 토큰을 쿠키에 설정
        Cookie accessTokenCookie = new Cookie("access_token", authResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600); // 1시간

        //리프레시 토큰 설정
        Cookie refreshTokenCookie = new Cookie("refresh_token", authResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(14 * 24 * 3600); // 14일

        //토큰에 저장
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.addHeader("Authorization", "Bearer " + authResponse.getAccessToken());


        //map 내용 세팅
        Map<String, Object> map2 = new HashMap<>();
        map2.put("success",true);
        map2.put("message","로그인 성공");
        map2.put("data",authResponse);

        return ResponseEntity.ok().body(map2);

    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        // 현재 인증 세션 제거
        SecurityContextHolder.clearContext();

        //System.out.println(">>> accessTokenCookie 존재");
        //String accessToken = accessTokenCookie.getValue();

        // 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        // 리프레시 토큰 쿠키도 삭제
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);


        //로그아웃 호출
        authService.logout(request);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("success",true);
        map2.put("message","성공적으로 로그아웃되었습니다.");
        map2.put("data",null);

        return ResponseEntity.ok().body(map2);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String token, HttpServletResponse response) {

        String refreshToken = token.replace("Bearer ", "");
        AuthResponse authResponse = authService.reissue(refreshToken);

        // 새로운 토큰을 쿠키에 설정
        Cookie accessTokenCookie = new Cookie("access_token", authResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600); // 1시간

        Cookie refreshTokenCookie = new Cookie("refresh_token", authResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(14 * 24 * 3600); // 14일

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        //map 내용 세팅
        Map<String, Object> map2 = new HashMap<>();
        map2.put("success",true);
        map2.put("message","토큰 갱신 성공");
        map2.put("data",authResponse.getAccessToken());

        return ResponseEntity.ok().body(map2);
        //return ResponseEntity.ok(responseBody);
        //return ResponseEntity.ok();
    }

    @GetMapping("/protected")
    public ResponseEntity<UserInfoDto> getUserProtected(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new UserInfoDto(userDetails));
    }

}
