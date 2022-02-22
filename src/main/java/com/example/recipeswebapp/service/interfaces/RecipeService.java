package com.example.recipeswebapp.service.interfaces;

import com.example.recipeswebapp.model.DTO.RecipeDTO;
import com.example.recipeswebapp.model.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> findAll();
    Optional<Recipe> findById(Long id);
    List<Recipe> findAllRecipesApprovedAndMostRecent();
    List<Recipe> findAllRecipesApprovedAndTrending();
    List<Recipe> findAllRecipesByUser(String username);
    Optional<Recipe> findByName(String name);
    Optional<Recipe> save(RecipeDTO recipeDTO, List<MultipartFile> images, List<String> imagesNames) throws IOException;
    void deleteById(Long id);

    Optional<Recipe> editRecipe(RecipeDTO recipeDTO, Long recipeId);
}
