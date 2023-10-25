package com.temat29zad1.controllers;

import com.temat29zad1.email.EmailService;
import com.temat29zad1.user.*;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        if (userService.checkIfEmailExists(email)) {
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            String endPointUrl = url + "/set-new-password/" + passwordResetService.generatePasswordResetToken(email);
            emailService.sendPasswordResetEmail(email, endPointUrl);
        }
        ra.addAttribute("message", "Jeżeli konto istnieje, na adres email został wysłany link.");
        return "redirect:/confirm";
    }

    @GetMapping("/set-new-password/{token}")
    public String setNewPassword(RedirectAttributes ra, @PathVariable(required = false) String token
            ,@RequestParam(name = "message", required = false) String message, Model model) {
        if (token != null && !token.equals("change")) {
            if (passwordResetService.checkIfTokenIsValid(token)) {
                model.addAttribute("userToken", token);
                return "new-password";
            }
        } else {
            model.addAttribute("message", message);
            return "new-password";
        }
        ra.addAttribute("message", "Link do ustawienia hasła stracił ważność!");
        return "redirect:/confirm";
    }

    @PostMapping("/set-new-password")
    public String setNewPassword(@CurrentSecurityContext SecurityContext securityContext,
                                 String userToken, String password, @RequestParam(required = false) String oldPassword,
                                 RedirectAttributes ra) {
        if (userToken.length() > 20) {
            if (userService.updateUserPasswordByToken(userToken, password)) {
                ra.addAttribute("message", "Hasło zostało zmienione");
                return "redirect:/confirm";
            }
        } else if (oldPassword != null) {
            if (userService.updatePassword(securityContext, password, oldPassword)) {
                ra.addAttribute("message", "Hasło zostało zmienione");
                return "redirect:/confirm";
            } else {
                ra.addAttribute("message", "Stare hasło jest niepoprawne");
                return "redirect:/set-new-password/change";
            }
        }
        return"redirect:/error";
    }
}
