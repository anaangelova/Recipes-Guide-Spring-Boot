package com.example.recipeswebapp.web;

import com.example.recipeswebapp.service.interfaces.AdminService;
import com.example.recipeswebapp.service.interfaces.RecipeService;
import com.example.recipeswebapp.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final AdminService adminService;
    public AdminController(RecipeService recipeService, UserService userService, AdminService adminService) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/approve/{id}")
    public String approveRecipe(@PathVariable Long id, HttpServletRequest request){
        adminService.approveRecipe(id);
        return "redirect:/recipes/myRecipes/"+ request.getRemoteUser();

    }

    @GetMapping("/deny/{id}")
    public String denyRecipe(@PathVariable Long id, HttpServletRequest request){
        adminService.denyRecipe(id);
        return "redirect:/recipes/myRecipes/"+ request.getRemoteUser();
    }
}
