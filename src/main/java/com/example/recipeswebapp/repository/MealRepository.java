package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
