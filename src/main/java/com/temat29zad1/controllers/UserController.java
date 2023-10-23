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

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String user (@CurrentSecurityContext SecurityContext securityContext, Model model) {
        if (securityContext.getAuthentication().getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            model.addAttribute("admin", "admin");
        } else {
            model.addAttribute("user", "user");
        }
        return "user";
    }

    @GetMapping("edit")
    public String edit(@CurrentSecurityContext SecurityContext securityContext, Model model) {
        UserDto userDto = userService.findUserDtoByEmail(securityContext.getAuthentication().getName()).get();
        if (securityContext.getAuthentication().getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            model.addAttribute("admin", true);
        } if (securityContext.getAuthentication().getAuthorities().toString().equals("[ROLE_USER]")) {
            model.addAttribute("user", "user");
        }
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
