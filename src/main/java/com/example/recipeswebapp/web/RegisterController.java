package com.example.recipeswebapp.web;


import com.example.recipeswebapp.model.DTO.UserRegistrationDTO;
import com.example.recipeswebapp.model.Identity.Role;
import com.example.recipeswebapp.model.exceptions.PasswordsDoNotMatchException;
import com.example.recipeswebapp.service.interfaces.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        return "register";

    }

    @PostMapping
    public String register(@Valid UserRegistrationDTO dto) {

        userService.register(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getRepeatPassword(), dto.getName(), dto.getSurname(), Role.ROLE_USER);
        return "redirect:/login";

    }
}
