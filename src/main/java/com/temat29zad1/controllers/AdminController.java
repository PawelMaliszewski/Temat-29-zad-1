package com.temat29zad1.controllers;

import com.temat29zad1.user.UserService;
import com.temat29zad1.user.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    private String adminPanel(Model model) {
        List<UserDto> usersByRole = userService.findAllUsers();
        model.addAttribute("dtoUserList", usersByRole);
        return "admin";
    }

    @GetMapping("/edit")
    public String editById(Long id, Model model) {
        model.addAttribute("userDto", userService.findUserDtoById(id));
        return "edit";
    }

    @PostMapping("/edit")
    public String reverseRole(Long id, Model model) {
        userService.reverseRole(id);
        List<UserDto> usersByRole = userService.findAllUsers();
        model.addAttribute("dtoUserList", usersByRole);
        return "admin";
    }
}
