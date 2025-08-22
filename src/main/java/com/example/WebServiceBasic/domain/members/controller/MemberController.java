package com.example.WebServiceBasic.domain.members.controller;

import com.example.WebServiceBasic.domain.members.dto.MemberDto;
import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.domain.members.error.MemberErrorCode;
import com.example.WebServiceBasic.domain.members.service.MemberItemCheck;
import com.example.WebServiceBasic.domain.members.service.MemberService;
import com.example.WebServiceBasic.domain.members.service.MemberServiceImpl;
import com.example.WebServiceBasic.domain.posts.dto.PageInfoDto;
import com.example.WebServiceBasic.domain.auth.role.Role;
import com.example.WebServiceBasic.domain.user.service.UserServiceImpl;
import com.example.WebServiceBasic.global.exception.ValidateHandling;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberItemCheck memberItemCheck;


    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ValidateHandling validateHandling;

    //목록
    @GetMapping("/members")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize, Model model){

        Page<MemberDto> postPage = memberService.getList(keyword,  page, pageSize);

        //페이징 처리
        int offset = (page - 1) * pageSize;
        int currentPage = postPage.getNumber()+1; //현재페이지
        int totalPages= postPage.getTotalPages(); //총페이지수
        long totalCounts = postPage.getTotalElements(); //전체 개시물수

        int blockPage = 10; //페이지 나타나는 갯수
        int startPage = ((currentPage-1)/blockPage)*blockPage+1; //블럭 첫페이지
        int endPage = Math.min(startPage + blockPage - 1, totalPages);; //블럭 마지막 페이지

        //페이지 숫자처리(1,2,3,4,5)
        List<PageInfoDto> pageNumbers = IntStream.range(startPage, endPage +1)
                .asLongStream().mapToObj(p -> {
                    return new PageInfoDto(p, p == currentPage); })
                .collect(Collectors.toList());

        int prevBlockPage = startPage - 1;
        int nextBlockPage = endPage + 1;

        //모델로 넘기기
        model.addAttribute("keyword", keyword != null ? keyword:""); //키워드
        model.addAttribute("members", postPage.getContent()); //내용 목록

        model.addAttribute("pageNumbers", pageNumbers); //페이지징 배열
        model.addAttribute("blockPage", blockPage); //현재페이지 번호
        model.addAttribute("currentPage", currentPage); //현재페이지 번호
        model.addAttribute("totalPages", totalPages); //전체 페이지수
        model.addAttribute("totalCounts", totalCounts); //전체 게시물수
        model.addAttribute("prevBlockPage", prevBlockPage > 0 ? prevBlockPage : null); //이전블럭 블록여부
        Model nextBlockPage1 = model.addAttribute("nextBlockPage", nextBlockPage <= totalPages ? nextBlockPage : null);//다음블럭 여부

        return "admin/members/list";
    }

    //상세내용
    @GetMapping("/members/{id}")
    public String read(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "1") int page,
                       @PathVariable Long id, Model model){

        //Member member = memberRepository.findByIdx(idx).orElse(null);
        Member  member = memberService.getRead(id);

        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("member", member);
        return "admin/members/read";
    }

    //수정폼
    @GetMapping("/members/{id}/edit")
    public String editForm(@RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "1") int page,
                           @PathVariable Long id, Model model){


        List<Role> rolesList = List.of(Role.values());

        Member member = memberService.getEditForm(id);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("member",member);
        model.addAttribute("roles",rolesList);

        return "admin/members/edit";
    }

    //수정하기
    @PostMapping("/members/{id}/edit")
    public String edit(@PathVariable Long id, MemberDto dto,
                       Errors errors, Model model){

        // 유효성 통과 못한 필드와 메시지를 핸들링
        if(dto.getRole() == null) {
            model.addAttribute("invalid_role", MemberErrorCode.ROLE_NOT_EXISTS.getMessage());

            // 회원가입 페이지로 다시 리턴
            return "/admin/members/edit";
        }

        Member target = memberService.edit(dto);
        return "redirect:/admin/members/"+dto.getId();
    }


    //삭제
    @GetMapping("/members/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){


        Member member = memberService.delete(id);

        if(member != null){
            rttr.addFlashAttribute("msg", "삭제되었습니다.");
        }
        return "redirect:/admin/members";
    }

}
