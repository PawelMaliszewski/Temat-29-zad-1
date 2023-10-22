package com.temat29zad1.controller;

import com.temat29zad1.roles.Role;
import com.temat29zad1.service.UserService;
import com.temat29zad1.user.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(UserDto userDto, Model model) {
        userDto.setRole(Role.USER);
        Boolean register = userService.register(userDto);
        if (register) {
            return "redirect:/confirm";
        }
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("error", "Użytkownik: " + userDto.getEmail() + " jest zarejestrowany");
        return "register";
    }

    @GetMapping("/confirm")
    public String conf() {
        return "confirm";
    }
}
