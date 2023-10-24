package com.temat29zad1.controllers;

import com.temat29zad1.email.EmailService;
import com.temat29zad1.user.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final UserService userService;
    private final EmailService emailService;

    public PasswordResetController(PasswordResetService passwordResetService, UserService userService, EmailService emailService) {
        this.passwordResetService = passwordResetService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("forgot-password")
    public String forgotPassword() {
        return "password-reset";
    }

    @PostMapping("/reset-password")
    public String passwordResetRequest(String email, RedirectAttributes ra) {
        Optional<UserDto> userByEmail = userService.findUserDtoByEmail(email);
        if (userByEmail.isPresent()) {
            passwordResetService.deleteExpiredTokenIfExists(userByEmail.get().getId());
            emailService.sendEmail(userByEmail.get());
        }
        ra.addAttribute("message", "Jeżeli konto istnieje, na adres email został wysłany link.");
        return "redirect:/confirm";
    }

    @GetMapping("/set-new-password/{token}")
    public String setNewPassword(Model model, @PathVariable String token) {
        Optional<PasswordResetToken> prt = passwordResetService.returnTokenIfValidated(token);
        if (prt.isPresent()) {
            model.addAttribute("userToken", token);
            return "new-password";
        }
        userService.deleteTokenByToken(token);
        return "redirect:/";
    }

    @PostMapping("/set-new-password")
    public String setNewPassword(String userToken, String password, RedirectAttributes ra) {
        if (userService.updateUser(null, userToken, password)) {
            ra.addAttribute("message", "Hasło zostało zmienione");
            return "redirect:/confirm";
        }
        return "redirect:/error";
    }
}
