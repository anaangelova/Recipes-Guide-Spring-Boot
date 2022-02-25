package com.example.recipeswebapp.web;

import com.example.recipeswebapp.model.*;
import com.example.recipeswebapp.model.DTO.EditDTO;
import com.example.recipeswebapp.model.DTO.RecipeDTO;
import com.example.recipeswebapp.model.DTO.ContentDTO;
import com.example.recipeswebapp.model.DTO.RecipeDetailsDTO;
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
    public String getAllRecipes(@RequestParam(required = false) boolean subscribed,
                                Model model){

        List<Recipe> mostRecent=recipeService.findAllRecipesApprovedAndMostRecent();
        List<Recipe> trending=recipeService.findAllRecipesApprovedAndTrending();
        model.addAttribute("recents",mostRecent);
        model.addAttribute("trendings",trending);
        if(subscribed) model.addAttribute("subscribed",true);

        return "recipes";
    }


    @GetMapping( "/addRecipe")
    public String addRecipe(Model model){
        ContentDTO toShow=this.getContent();
        model.addAttribute("dto",toShow);
        List<String> allMeasurements= Arrays.stream(Measurement.values()).map(m -> m.name()).collect(Collectors.toList());
        model.addAttribute("measurements",allMeasurements);
        return "add-recipe";
    }

    @PostMapping("/addRecipe") //dali rabote validacijata???
    public String addRecipePost(@Valid RecipeDTO recipeAdded, @RequestPart List<MultipartFile> images) throws IOException {
        List<String> imagesNames=this.saveImages(images);
        recipeService.save(recipeAdded,imagesNames);
        return "redirect:/recipes";
    }

    @GetMapping("/details/{id}")
    public String getDetailsForRecipe(@PathVariable Long id,Model model){

        RecipeDetailsDTO detailsDTO=this.getDetailsDTO(id);
        model.addAttribute("detailsDTO",detailsDTO);
        return "recipeDetails";

    }

    @GetMapping("/search")
    public String searchForResults(@RequestParam String searchInput){

        List<Recipe> recipes=recipeService.findBySearch(searchInput);

        return "redirect:/recipes";
    }

    @GetMapping("/myRecipes/{id}")
    public String getUsersRecipes(@PathVariable String id,Model model, HttpServletRequest request){
        List<Recipe> recipesByUser=recipeService.findAllRecipesByUser(id);
        model.addAttribute("recipes",recipesByUser);
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

        Recipe toEdit=recipeService.findById(id);
        if(isAuthor(toEdit,request.getRemoteUser())){
            ContentDTO contentDTO=this.getContent();
            EditDTO editDTO=this.getEditDTO(toEdit);
            model.addAttribute("contentDTO",contentDTO);
            model.addAttribute("editDTO",editDTO);
            List<String> allMeasurements= Arrays.stream(Measurement.values()).map(m -> m.name()).collect(Collectors.toList());
            model.addAttribute("measurements",allMeasurements);
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

    @GetMapping("/removeFromSaved/{id}")
    public String removeFromSaved(@PathVariable Long id, HttpServletRequest request){

        recipeService.removeFromSaved(id,request.getRemoteUser());
        return "redirect:/recipes/myRecipes/"+request.getRemoteUser();

    }

    @GetMapping("/{cat}/{val}")
    public String getByCategoryAndValue(@PathVariable String val, @PathVariable String cat, Model model){

        List<Recipe> recipesList=recipeService.findAllRecipesByCategoryAndSubCategory(cat,val);
        model.addAttribute("recipes",recipesList);
        model.addAttribute("val",val);
        return "recipes-category";

    }
    @GetMapping("/byAuthor/{author}")
    public String getByAuthor(@PathVariable String author, Model model){
        List<Recipe> recipesList=recipeService.findAllRecipesByUser(author);
        model.addAttribute("recipes",recipesList);
        model.addAttribute("val",author);
        model.addAttribute("forAuthor",true);
        return "recipes-category";

    }

    private ContentDTO getContent(){
        List<Meal> allMeals=mealService.getAllMeals();
        List<Cuisine> allCuisines=cuisineService.getAllCuisines();
        List<SpecialConsideration> allCons=specialConsService.getAllSpecConsiderations();
        List<String> allMeasurements= Arrays.stream(Measurement.values()).map(m -> m.name()).collect(Collectors.toList());
        return new ContentDTO(allMeals,allCuisines,allCons,allMeasurements);
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
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
        return imagesNames;
    }

    private RecipeDetailsDTO getDetailsDTO(Long id){
        Recipe toShow=recipeService.findById(id);
        List<String> ingredients=toShow.getIngredients().stream().map(i -> i.getFormatted()).collect(Collectors.toList());
        List<String> instructions=toShow.getInstructions().stream().map(instr -> instr.getDescription()).collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDateTime = toShow.getDateCreated().format(formatter);

        Set<String> allTags = new TreeSet<>(toShow.getTagList().stream().map(t -> t.getName().toUpperCase()).collect(Collectors.toList()));
        allTags.add(toShow.getMeal().getName().toUpperCase());
        allTags.add(toShow.getCuisine().getName().toUpperCase());

        return new RecipeDetailsDTO(allTags,instructions,ingredients,formatDateTime,toShow);

    }
    private boolean isAuthor(Recipe toEdit, String author){
        return author.equals(toEdit.getAuthor().getUsername());
    }

    private EditDTO getEditDTO(Recipe toEdit){
        String keywordsS=toEdit.getTagList().stream().map(t -> t.getName()).collect(Collectors.joining(" "));
        int prepH= (int) (toEdit.getPrepInMins()/60);
        int cookH= (int) (toEdit.getCookInMins()/60);
        return new EditDTO(toEdit,keywordsS,prepH,cookH);
    }
}
