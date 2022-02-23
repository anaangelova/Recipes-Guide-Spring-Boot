package com.example.recipeswebapp.service.interfaces;

public interface AdminService {

    boolean approveRecipe(Long id);
    boolean denyRecipe(Long id);
}
