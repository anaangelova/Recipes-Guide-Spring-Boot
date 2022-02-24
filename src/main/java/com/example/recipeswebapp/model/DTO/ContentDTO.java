package com.example.recipeswebapp.model.DTO;

import com.example.recipeswebapp.model.Cuisine;
import com.example.recipeswebapp.model.Meal;
import com.example.recipeswebapp.model.Measurement;
import com.example.recipeswebapp.model.SpecialConsideration;
import lombok.Data;

import java.util.List;

@Data
public class ContentDTO {
    private List<Meal> allMeals;
    private List<Cuisine> allCuisines;
    private List<SpecialConsideration> allCons;
    private List<String> allMeasurements;

    public ContentDTO(List<Meal> allMeals, List<Cuisine> allCuisines, List<SpecialConsideration> allCons, List<String> allMeasurements) {
        this.allMeals = allMeals;
        this.allCuisines = allCuisines;
        this.allCons = allCons;
        this.allMeasurements = allMeasurements;
    }
}
