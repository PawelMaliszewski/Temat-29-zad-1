package com.temat29zad1.controllers;

import com.temat29zad1.user.UserFullNameDto;
import com.temat29zad1.user.UserService;
import com.temat29zad1.user.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String user() {
        return "user";
    }

    @GetMapping("edit")
    public String edit(@CurrentSecurityContext SecurityContext securityContext, Model model) {
        UserDto userDto = userService.findUserDtoByEmail(securityContext.getAuthentication().getName()).get();
        model.addAttribute("userDto", userDto);
        return "edit";
    }

    @PostMapping("/update")
    public String updateUser(Authentication authentication, UserFullNameDto userDto) {
        String currentUserEmail = authentication.getName();
        userService.updateUsersFullName(userDto, currentUserEmail);
        return "redirect:/user";
    }
}

