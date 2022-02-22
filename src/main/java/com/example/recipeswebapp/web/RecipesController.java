package com.example.recipeswebapp.web;

import com.example.recipeswebapp.model.*;
import com.example.recipeswebapp.model.DTO.RecipeDTO;
import com.example.recipeswebapp.service.interfaces.CuisineService;
import com.example.recipeswebapp.service.interfaces.MealService;
import com.example.recipeswebapp.service.interfaces.RecipeService;
import com.example.recipeswebapp.service.interfaces.SpecialConsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipes")
public class RecipesController {

    private final MealService mealService;
    private final CuisineService cuisineService;
    private final SpecialConsService specialConsService;

    private final RecipeService recipeService;
    public RecipesController(MealService mealService, CuisineService cuisineService, SpecialConsService specialConsService, RecipeService recipeService) {
        this.mealService = mealService;
        this.cuisineService = cuisineService;
        this.specialConsService = specialConsService;
        this.recipeService = recipeService;
    }

    @GetMapping()
    public String getAllRecipes(Model model){
        //zemi gi site recepti koi se approved i smesti gi vo 2 listi: trending now, most recent
        List<Recipe> mostRecent=recipeService.findAllRecipesApprovedAndMostRecent();
        List<Recipe> trending=recipeService.findAllRecipesApprovedAndTrending();
        model.addAttribute("recents1",mostRecent.subList(0,4));
        model.addAttribute("recents2",mostRecent.subList(4,8));
        model.addAttribute("trendings1",trending.subList(0,4));
        model.addAttribute("trendings2",trending.subList(4,8));
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
    public String addRecipePost(@Valid RecipeDTO recipeAdded, @RequestPart List<MultipartFile> images) throws IOException {

        String path="";
        List<String> imagesNames=new ArrayList<>();
        for(MultipartFile m: images){
            path = "C:\\Users\\Latinka\\IdeaProjects\\RecipesWebApp\\src\\main\\resources\\static\\images\\"+m.getOriginalFilename();
            File newFile = new File(path);
            newFile.createNewFile();
            FileOutputStream myfile = new FileOutputStream(newFile);
            myfile.write(m.getBytes());
            myfile.close();
            imagesNames.add(m.getOriginalFilename());
        }
        recipeService.save(recipeAdded,images,imagesNames);
        return "redirect:/recipes";
    }

    @GetMapping("/details/{id}")
    public String getDetailsForRecipe(@PathVariable Long id,Model model){
        Recipe toShow=recipeService.findById(id).orElseThrow();
        model.addAttribute("recipe",toShow);
        return "details";

    }
    @GetMapping("/myRecipes/{id}")
    public String getUsersRecipes(@PathVariable String id,Model model){
        List<Recipe> selectedRecipes=recipeService.findAllRecipesByUser(id);
        model.addAttribute("recipes",selectedRecipes);
        return "my-recipes";
    }


    @GetMapping("/edit/{id}")
    public String editRecipe(@PathVariable Long id, Model model, HttpServletRequest request){

        Recipe toShow=recipeService.findById(id).orElseThrow();
        if(request.getRemoteUser().equals(toShow.getAuthor().getUsername())){
            model.addAttribute("recipe",toShow);
            List<Meal> allMeals=mealService.getAllMeals();
            List<Cuisine> allCuisines=cuisineService.getAllCuisines();
            List<SpecialConsideration> allCons=specialConsService.getAllSpecConsiderations();
            List<String> allMeasurements= Arrays.stream(Measurement.values()).map(m -> m.name()).collect(Collectors.toList());
            model.addAttribute("meals",allMeals);
            model.addAttribute("cuisines",allCuisines);
            model.addAttribute("specs",allCons);
            model.addAttribute("measurements",allMeasurements);
            String keywordsS=toShow.getTagList().stream().map(t -> t.getName()).collect(Collectors.joining(" "));
            model.addAttribute("keywords",keywordsS);
            int prepH= (int) (toShow.getPrepInMins()/60);
            int cookH= (int) (toShow.getCookInMins()/60);
            model.addAttribute("prepH",prepH);
            model.addAttribute("cookH",cookH);
            return "edit";
        }else return "redirect:/recipes";


    }
    @PostMapping("/editRecipe")
    public String editRecipePost(@Valid RecipeDTO recipeEdited, Long recipeId, String author){

        recipeService.editRecipe(recipeEdited,recipeId);
        return "redirect:/recipes/myRecipes/"+author;
    }


}
