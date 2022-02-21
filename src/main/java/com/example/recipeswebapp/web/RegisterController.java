package com.example.recipeswebapp.web;


import com.example.recipeswebapp.model.Identity.Role;
import com.example.recipeswebapp.service.interfaces.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model){
        if(error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
       /* model.addAttribute("bodyContent","register"); //register.html ja davame vsushnost*/
        return "register";

    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String repeatPassword,
                           @RequestParam String name,
                           @RequestParam String surname){
        try{
            userService.register(username, email, password, repeatPassword, name, surname, Role.ROLE_USER);
            //po default da bide USER! Vo aud mozhe da bira

            return "redirect:/login";
        }catch (BadCredentialsException exception){
            return "redirect:/register?error="+exception.getMessage();
        }
    }
}
