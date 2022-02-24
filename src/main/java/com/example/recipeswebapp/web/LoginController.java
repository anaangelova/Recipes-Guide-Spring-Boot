package com.example.recipeswebapp.web;


import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import com.example.recipeswebapp.model.exceptions.InvalidUserCredentialsException;
import com.example.recipeswebapp.service.interfaces.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage(){

        return "login";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model){
        RecipeAuthor user=null;
        try{
            user = authService.login(request.getParameter("username"),request.getParameter("password"));
            request.getSession().setAttribute("user",user);
            return "redirect:/home";
        }catch (InvalidUserCredentialsException exception){
            model.addAttribute("hasError",true);
            model.addAttribute("error",exception.getMessage());
            return "login";
        }
    }
}
