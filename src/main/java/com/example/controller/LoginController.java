package com.example.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String message1 = "";
            model.addAttribute("message1", message1);
            return "loginPage.html";
        }
        String message2 = "yes";
        model.addAttribute("message2", message2);
        return "home.html";
    }

    @GetMapping("/login-fail")
    public String loginPage1(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String message1 = "invUser";
            model.addAttribute("message1", message1);
            return "loginPage.html";
        }
        String message2 = "yes";
        model.addAttribute("message2", message2);
        return "home.html";
    }

    @GetMapping("/logout-success")
    public String loginPage2(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String message1 = "logSuccess";
            model.addAttribute("message1", message1);
            return "loginPage.html";
        }
        String message2 = "yes";
        model.addAttribute("message2", message2);
        return "home.html";
    }

}
