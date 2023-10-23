package com.temat29zad1.controllers;

import com.temat29zad1.service.UserService;
import com.temat29zad1.user.UserDto;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    private String adminPanel(@CurrentSecurityContext SecurityContext securityContext, Model model) {
        List<UserDto> usersByRole = userService.findAllUsers();
        model.addAttribute("user", "admin");
        model.addAttribute("dtoUserList", usersByRole);
        return "admin";
    }

    @GetMapping("/{id}")
    public String editById(Long id, Model model) {
        model.addAttribute("userDto", userService.findUserDtoById(id));
        model.addAttribute("admin", true);
        return "edit";
    }
}
