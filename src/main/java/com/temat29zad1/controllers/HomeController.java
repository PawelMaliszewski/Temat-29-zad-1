package com.temat29zad1.controllers;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@CurrentSecurityContext SecurityContext securityContext, Model model) {
        if (securityContext.getAuthentication().getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            model.addAttribute("user", "admin");
        }
        if (securityContext.getAuthentication().getAuthorities().toString().equals("[ROLE_USER]")) {
            model.addAttribute("user", "user");
        }
        return "index";
    }
}
