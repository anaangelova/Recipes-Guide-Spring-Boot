package com.example.recipeswebapp.service.interfaces;

import com.example.recipeswebapp.model.Identity.RecipeAuthor;

public interface AuthService {

    RecipeAuthor login(String username, String password);
}
