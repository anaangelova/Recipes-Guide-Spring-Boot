package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository  extends JpaRepository<Ingredient,Long> {
}
