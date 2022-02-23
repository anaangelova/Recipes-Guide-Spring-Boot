package com.example.recipeswebapp.service.implementation;


import com.example.recipeswebapp.model.Recipe;
import com.example.recipeswebapp.model.RecipeStatus;
import com.example.recipeswebapp.repository.RecipeRepository;
import com.example.recipeswebapp.service.interfaces.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private final RecipeRepository recipeRepository;

    public AdminServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    @Override
    public boolean approveRecipe(Long id) {
        Recipe toApprove=recipeRepository.findById(id).orElseThrow();
        toApprove.setStatus(RecipeStatus.APPROVED);
        recipeRepository.save(toApprove);
        return true;
    }

    @Transactional
    @Override
    public boolean denyRecipe(Long id) {
        Recipe toApprove=recipeRepository.findById(id).orElseThrow();
        toApprove.setStatus(RecipeStatus.DENIED);
        recipeRepository.save(toApprove);
        return true;
    }
}
