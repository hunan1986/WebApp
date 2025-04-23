package com.example.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homepage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String message2 = "no";
            model.addAttribute("message2", message2);
            return "home.html";
        }
        String message2 = "yes";
        model.addAttribute("message2", message2);
        return "home.html";
    }

    @GetMapping("/empAccessPage")
    public String empPage() {
        return "employeeAccessPage.html";
    }

}
