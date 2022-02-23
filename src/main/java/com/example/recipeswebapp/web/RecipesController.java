package com.example.recipeswebapp.web;

import com.example.recipeswebapp.model.*;
import com.example.recipeswebapp.model.DTO.RecipeDTO;
import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import com.example.recipeswebapp.model.Identity.Role;
import com.example.recipeswebapp.service.interfaces.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipes")
public class RecipesController {

    private final MealService mealService;
    private final CuisineService cuisineService;
    private final SpecialConsService specialConsService;
    private final UserService userService;

    private final RecipeService recipeService;
    public RecipesController(MealService mealService, CuisineService cuisineService, SpecialConsService specialConsService, UserService userService, RecipeService recipeService) {
        this.mealService = mealService;
        this.cuisineService = cuisineService;
        this.specialConsService = specialConsService;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @GetMapping()
    public String getAllRecipes(Model model){

        List<Recipe> mostRecent=recipeService.findAllRecipesApprovedAndMostRecent();
        List<Recipe> trending=recipeService.findAllRecipesApprovedAndTrending();
        model.addAttribute("recents",mostRecent);
        model.addAttribute("trendings",trending);

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDateTime = toShow.getDateCreated().format(formatter);
        model.addAttribute("recipe",toShow);
        model.addAttribute("formatted",formatDateTime);
        List<String> tmp=toShow.getIngredients().stream().map(i -> i.getFormatted()).collect(Collectors.toList());
        List<String> instructions=toShow.getInstructions().stream().map(instr -> instr.getDescription()).collect(Collectors.toList());
        model.addAttribute("allIngr",tmp);
        model.addAttribute("allInstr",instructions);

        Set<String> allTags=new TreeSet<>();
        allTags.addAll(toShow.getTagList().stream().map(t -> t.getName()).collect(Collectors.toList()));
        allTags.add(toShow.getMeal().getName());
        allTags.add(toShow.getCuisine().getName());
        allTags=allTags.stream().map(t -> t.toUpperCase()).collect(Collectors.toSet());
        model.addAttribute("allTags",allTags);

        return "recipeDetails";

    }
    @GetMapping("/myRecipes/{id}")
    public String getUsersRecipes(@PathVariable String id,Model model, HttpServletRequest request){
        List<Recipe> selectedRecipes=recipeService.findAllRecipesByUser(id);
        model.addAttribute("recipes",selectedRecipes);
        RecipeAuthor author=(RecipeAuthor) userService.loadUserByUsername(request.getRemoteUser());
        model.addAttribute("author",author);
        if(author.getRole().equals(Role.ROLE_ADMIN)){
            List<Recipe> pending=recipeService.findPendingPapers();
            model.addAttribute("pending",pending);

        }

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

    @DeleteMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id, HttpServletRequest request){

        this.recipeService.deleteById(id);
        return "redirect:/recipes/myRecipes/"+request.getRemoteUser();
    }

    @GetMapping("/addToSaved/{id}")
    public String addToSaved(@PathVariable Long id, HttpServletRequest request){
        if(request.getRemoteUser()==null){
            return "redirect:/login";
        }
        recipeService.addRecipeToSaved(id,request.getRemoteUser());
        return "redirect:/recipes";

    }
    //removeFromSaved
    @GetMapping("/removeFromSaved/{id}")
    public String removeFromSaved(@PathVariable Long id, HttpServletRequest request){

        recipeService.removeFromSaved(id,request.getRemoteUser());
        return "redirect:/recipes/myRecipes/"+request.getRemoteUser();

    }

}
