package com.example.recipeswebapp.service.interfaces;

import com.example.recipeswebapp.config.CustomOAuth2User;
import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import com.example.recipeswebapp.model.Identity.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    RecipeAuthor register(String username, String email, String password, String repeatPassword, String firstName, String lastName, Role role);

    void processOAuthPostLogin(CustomOAuth2User oauthUser);
}
