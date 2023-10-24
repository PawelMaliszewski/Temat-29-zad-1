package com.temat29zad1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam(value = "message", required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "/confirm";
    }
}
