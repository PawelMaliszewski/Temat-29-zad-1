package com.temat29zad1.controllers;

import com.temat29zad1.user.UserService;
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
        Boolean register = userService.registerUser(userDto);
        if (register) {
            return "redirect:/confirm?message=Konto utworzone.";
        }
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("error", "Użytkownik: " + userDto.getEmail() + " jest zarejestrowany");
        return "register";
    }
}
