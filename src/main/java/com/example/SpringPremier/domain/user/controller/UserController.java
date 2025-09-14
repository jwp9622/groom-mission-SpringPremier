package com.example.SpringPremier.domain.user.controller;

import com.example.SpringPremier.domain.auth.role.Role;
import com.example.SpringPremier.domain.auth.service.LoginService;
import com.example.SpringPremier.domain.members.dto.MemberDto;
import com.example.SpringPremier.domain.members.entity.Member;
import com.example.SpringPremier.domain.members.service.MemberItemCheck;
import com.example.SpringPremier.domain.members.service.MemberService;
import com.example.SpringPremier.domain.user.error.UserErrorCode;
import com.example.SpringPremier.domain.user.service.UserService;
import com.example.SpringPremier.global.exception.ValidateHandling;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberItemCheck memberItemCheck;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidateHandling validateHandling;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    //회원 mypage
    @GetMapping("/user/mypage")
    public String mypage(MemberDto dto, Model model){

        //jwt에서 아이디 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null){
            throw new ValidationException("유효한 사용자가 아닙니다.");
        }

        //jwt에서 아이디 가져오기
        Member member = memberItemCheck.getUserIdRead(username);
        model.addAttribute("member", member);

        return "user/mypage";
    }


    //정보 수정폼
    @GetMapping("/user/edit")
    public String editForm(HttpServletRequest request, Model model){

        //HttpSession session = request.getSession();
        //String memberId = session.getAttribute("memberId").toString();
        //if(session.getAttribute("memberId") == null){
        //    return null;
        //}

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null){
            throw new ValidationException("유효한 사용자가 아닙니다.");
        }

        //jwt에서 아이디 가져오기
        Member member = memberItemCheck.getUserIdRead(username);
        model.addAttribute("member", member);

        return "user/edit";
    }

    //정보 수정
    @PostMapping("/user/edit")
    public String edit(HttpServletRequest request,
                       @Valid MemberDto dto, Errors errors, Model model){

        //세션 체크
        //HttpSession session = request.getSession();
        //String memberId = session.getAttribute("memberId").toString();
        //if(session.getAttribute("memberId") == null){
        //    return null;
        //}

        //jwt에서 아이디 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null){
            throw new ValidationException("유효한 사용자가 아닙니다.");
        }

        //아이디 할당
        dto.setMemberId(username);


        //비밀번호 암호화
        dto.setRole(String.valueOf(Role.ROLE_USER));

        
        // 이메일 중복확인, 수정시에는 본인 이메일은 제외한다.
        boolean exists_email =  memberItemCheck.checkEmailIsNotId(dto.getEmail(), dto.getId());
        if(exists_email){
            errors.rejectValue("email","이메일중복", UserErrorCode.PASSWORD_NOT_MISMATCH.getMessage());
        }


        //등록 양식 중복체크
        if (errors.hasErrors()) {

            // 회원가입 실패시 입력 데이터 값을 유지
            model.addAttribute("member",dto);

            if(!dto.getPassword().equals(dto.getConfirm_password())){
                errors.rejectValue("confirm_password", "비밀번호 일치 오류", UserErrorCode.PASSWORD_NOT_MISMATCH.getMessage());
            }

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling.validateHandling("invalid",errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            // 회원가입 페이지로 다시 리턴
            return "/user/edit";
        }


        int updatedCount = userService.userEdit(dto);

        return "redirect:/user/edit";
    }


    //회원 탈퇴폼
    @GetMapping("/user/delete")
    public String deleteForm(HttpServletRequest request, Model model){
/*
        HttpSession session = request.getSession();
        String memberId = session.getAttribute("memberId").toString();
        if(session.getAttribute("memberId") == null){
            return null;
        }
*/
        //jwt에서 아이디 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null){
            throw new ValidationException("유효한 사용자가 아닙니다.");
        }

        return "user/delete";
    }


    //회원 탈퇴
    @PostMapping("/user/delete")
    public String delete(HttpServletRequest request, MemberDto dto, HttpServletRequest http, Model model){


        //HttpSession session = request.getSession();
        //String memberId = session.getAttribute("memberId").toString();
        //if(session.getAttribute("memberId") == null){
        //    return null;
        //}

        //jwt에서 아이디 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null){
            throw new ValidationException("유효한 사용자가 아닙니다.");
        }

        Member member = userService.findMemberId(username);
        if(member != null){
            
            //비밀번호 일치여부 확인
            boolean isMatch = bCryptPasswordEncoder.matches(dto.getPassword(),member.getPassword());

            if(!isMatch){   //비밀번호가 일치하지 않으면
                model.addAttribute("invalid_password",UserErrorCode.PASSWORD_NOT_MISMATCH.getMessage());
                return "user/delete";
            }else{  //비밀번호가 일치하면

                //아이디 정보 등록
                dto.setMemberId(username);

                //회원 삭제
                Member member_delete = userService.userDelete(username);

                //로그아웃 처리
                //loginService.logout(http);

                return "user/deletemsg";
            }

        }

        return "redirect:/";
    }



}
