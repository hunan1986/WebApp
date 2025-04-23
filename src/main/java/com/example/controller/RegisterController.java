package com.example.controller;

import com.example.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @GetMapping("/register")
    public String register(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            String message1 = "logSuccess";
            model.addAttribute("message1", message1);
            return "register.html";
        }
        String message2 = "yes";
        model.addAttribute("message2", message2);
        return "home.html";
    }

    @PostMapping("/register-handler")
    public String register(HttpServletRequest request, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm_password = request.getParameter("confirm_password");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {

            String message3 = registerService.registerHandler(username, password, confirm_password);

            if (!message3.equals("pass")) {
                model.addAttribute("message3", message3);
                return "register.html";
            }

            String message1 = "reg_info";
            model.addAttribute("message1", message1);
            return "loginPage.html";
        }
        String message2 = "yes";
        model.addAttribute("message2", message2);
        return "home.html";
    }
}
