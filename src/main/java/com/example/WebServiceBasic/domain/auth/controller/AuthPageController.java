package com.example.WebServiceBasic.domain.auth.controller;

import com.example.WebServiceBasic.domain.auth.service.LoginService;
import com.example.WebServiceBasic.domain.auth.service.LoginServiceImpl;
import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.service.MemberItemCheck;
import com.example.WebServiceBasic.domain.members.service.MemberService;
import com.example.WebServiceBasic.domain.user.service.UserService;
import com.example.WebServiceBasic.global.exception.ValidateHandling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AuthPageController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberItemCheck memberItemCheck;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidateHandling validateHandling;


    //로그인
    @GetMapping("/login")
    private String loginForm(Model model){
        return "login/login";
    }


    //회원가입양식
    @GetMapping("/signup")
    public String signForm(MemberDto dto, Model model){

        /* 회원가입 실패시 입력 데이터 값을 유지 */
        model.addAttribute("member",dto);

        return "signup/signup";
    }


/*
    //시큐리티 적용하면 작동안됨.
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDto dto, HttpServletRequest http, Model model){

        Member member = loginService.login(dto, http);

        if(member == null){
            model.addAttribute("msg",AuthErrorCode.ID_PASSWORD_NOT_MISMATCH.getMessage());
            return "login/login";
        }

        return "";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        loginService.logout(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }
*/


/*
    //회원 db에 등록하기
    @PostMapping("/signup")
    public String signUp(@Valid MemberDto dto,
                         Errors errors, Model model){

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

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            // 회원가입 페이지로 다시 리턴
            return "/signup/signup";
        }

        //권한 할당
        dto.setRole(String.valueOf(Role.ROLE_USER));

        //회원가입
        Member saved = memberService.create(dto);

        //return "";
        return "redirect:/";

    }
*/

}
