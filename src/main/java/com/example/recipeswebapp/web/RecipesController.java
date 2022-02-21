package com.example.recipeswebapp.web;

import com.example.recipeswebapp.model.Cuisine;
import com.example.recipeswebapp.model.Meal;
import com.example.recipeswebapp.model.Measurement;
import com.example.recipeswebapp.model.SpecialConsideration;
import com.example.recipeswebapp.service.interfaces.CuisineService;
import com.example.recipeswebapp.service.interfaces.MealService;
import com.example.recipeswebapp.service.interfaces.SpecialConsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipes")
public class RecipesController {

    private final MealService mealService;
    private final CuisineService cuisineService;
    private final SpecialConsService specialConsService;

    public RecipesController(MealService mealService, CuisineService cuisineService, SpecialConsService specialConsService) {
        this.mealService = mealService;
        this.cuisineService = cuisineService;
        this.specialConsService = specialConsService;
    }

    @GetMapping
    public String getAllRecipes(Model model){
        return "recipes";
    }

    @GetMapping( "/addRecipe")
    public String addRecipe(Model model){
        List<Meal> allMeals=mealService.getAllMeals();
        List<Cuisine> allCuisines=cuisineService.getAllCuisines();
        List<SpecialConsideration> allCons=specialConsService.getAllSpecConsiderations();
        List<String> allMeasurements= Arrays.stream(Measurement.values()).map(m -> m.name()).collect(Collectors.toList());
        model.addAttribute("meals",allMeals);
        model.addAttribute("cuisines",allCuisines);
        model.addAttribute("specs",allCons);
        model.addAttribute("measurements",allMeasurements);
        return "add-recipe";
    }

    @PostMapping("/addRecipe")
    public String addRecipePost(@RequestParam String title,
                                @RequestParam String desc,
                                @RequestParam Long meal,
                                @RequestParam Long cuisine,
                                @RequestParam String keywords,
                                @RequestParam Double prep,
                                @RequestParam Double prepMin,
                                @RequestParam List<String> instructions,
                                @RequestParam List<Long> cons,
                                @RequestParam List<Long> ingredientsQuantity,
                                @RequestParam List<String> measurementItem,
                                @RequestParam List<String> ingredients){

        return "redirect:/recipes";
    }

}
