package com.temat29zad1.controller;

import com.temat29zad1.roles.Role;
import com.temat29zad1.service.UserService;
import com.temat29zad1.user.User;
import com.temat29zad1.user.UserDto;
import org.springframework.core.NativeDetector;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String user(Model model) {
        return "user";
    }

    @GetMapping("edit")
    public String edit(@CurrentSecurityContext SecurityContext securityContext, Model model) {
        UserDto userDto = userService.findUserDtoByEmail(securityContext.getAuthentication().getName()).get();
        userDto.setPassword("");
        model.addAttribute("userDto", userDto);
        return "edit";
    }

    @PostMapping("/update")
    public String updateUser(UserDto userDto) {
        Boolean b = userService.updateUser(userDto);
        if (b) {
            return "user";
        }
        return "error";
    }
}
