package com.temat29zad1.controllers;

import com.temat29zad1.service.EmailService;
import com.temat29zad1.service.PasswordResetService;
import com.temat29zad1.passwordResetservice.PasswordResetToken;
import com.temat29zad1.service.UserService;
import com.temat29zad1.user.User;
import com.temat29zad1.user.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class PasswordReset {


    private final UserService userService;
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;


    public PasswordReset(UserService userService, EmailService emailService, PasswordResetService passwordResetService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordResetService = passwordResetService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("forgot-password")
    public String forgotPassword() {
        return "password-reset";
    }

    @PostMapping("/reset-password")
    public String passwordResetRequest(String email) {
        Optional<User> userByEmail = userService.findUserByEmail(email);
        if (userByEmail.isPresent()) {
            System.out.println();
            emailService.sendEmail(userByEmail.get());
        }
        return "index";
    }

    @GetMapping("/set-new-password/{token}")
    public String setNewPassword(Model model, @PathVariable String token) {
        Optional<PasswordResetToken> prt = passwordResetService.returnTokenIfValidated(token);
        if (prt.isPresent()) {
            model.addAttribute("userToken", prt.get().getToken());
            return "new-password";
        }
        userService.deleteTokenByToken(token);
        return "redirect:/token-expired";
    }

    @PostMapping("/set-new-password")
    public String setNewPassword(String userToken, String password) {
        Optional<PasswordResetToken> prt = passwordResetService.returnTokenIfValidated(userToken);
        if (prt.isPresent()) {
            User user = prt.get().getUser();
            if (password != null) {
                user.setPassword(passwordEncoder.encode(password));
            }
            Boolean saved = userService.updateUser(UserMapper.convertToUserDto(user));
            if (saved) {
                return "redirect:/confirm";
            }
        }
        return "redirect:error";
    }
}
