package com.example.WebServiceBasic.domain.web;

import com.example.WebServiceBasic.domain.members.entity.Member;
import com.example.WebServiceBasic.global.adapter.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Controller
public class WebController {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @GetMapping("/")
    public String main(@ModelAttribute("loginUser") Member loginUser, Model model){

        model.addAttribute("loginUser", loginUser);

        return "main";
    }

    @GetMapping("/intro")
    public String intro(){
        return "intro";
    }

    @GetMapping("/contactus")
    public String contactus(){
        return "contactus";
    }

}
