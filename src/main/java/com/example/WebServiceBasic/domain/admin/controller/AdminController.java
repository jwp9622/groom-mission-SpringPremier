package com.example.WebServiceBasic.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String AdminMain(Model model){

        System.out.println("test");
        return "admin/main";

    }
}
