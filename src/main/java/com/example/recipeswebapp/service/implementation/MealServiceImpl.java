package com.example.recipeswebapp.service.implementation;

import com.example.recipeswebapp.model.Meal;
import com.example.recipeswebapp.repository.MealRepository;
import com.example.recipeswebapp.service.interfaces.MealService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }
}
