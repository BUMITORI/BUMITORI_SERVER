package org.example.bumitori_server.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminController {

    @GetMapping("/admin")
    @ResponseBody
    public String adminAPI() {
        return "adminPage";
    }

    @GetMapping("/login")
    @ResponseBody
    public String loginPage() {
        return "loginPage";
    }



}
