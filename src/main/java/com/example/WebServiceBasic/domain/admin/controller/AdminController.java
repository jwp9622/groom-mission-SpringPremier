package com.example.WebServiceBasic.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String AdminMain(Model model){

        System.out.println("test11");
        System.out.println("test22");
        return "admin/main";

    }
}
