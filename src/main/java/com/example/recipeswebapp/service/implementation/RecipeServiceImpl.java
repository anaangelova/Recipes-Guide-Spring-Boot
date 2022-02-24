package com.example.recipeswebapp.service.implementation;

import com.example.recipeswebapp.model.*;
import com.example.recipeswebapp.model.DTO.RecipeDTO;
import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import com.example.recipeswebapp.model.exceptions.RecipeNotFoundException;
import com.example.recipeswebapp.repository.*;
import com.example.recipeswebapp.service.interfaces.RecipeService;
import com.example.recipeswebapp.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CuisineRepository cuisineRepository;
    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final SpecialConsiderationRepository specialConsiderationRepository;
    private final InstructionRepository instructionRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository, CuisineRepository cuisineRepository, MealRepository mealRepository, IngredientRepository ingredientRepository, SpecialConsiderationRepository specialConsiderationRepository, InstructionRepository instructionRepository, TagRepository tagRepository, ImageRepository imageRepository, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.cuisineRepository = cuisineRepository;
        this.mealRepository = mealRepository;

        this.ingredientRepository = ingredientRepository;
        this.specialConsiderationRepository = specialConsiderationRepository;
        this.instructionRepository = instructionRepository;
        this.tagRepository = tagRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe findById(Long id) {

        return recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @Override
    public List<Recipe> findAllRecipesApprovedAndMostRecent() {
        return recipeRepository.findMostRecentRecipes();

    }

    @Override
    public List<Recipe> findAllRecipesApprovedAndTrending() {
        List<Recipe> all = recipeRepository.findAllByStatus(RecipeStatus.APPROVED);
        Collections.shuffle(all);
        if (all.size() > 4)
            return all.subList(0, 4);
        else return all;

    }

    @Override
    public List<Recipe> findAllRecipesByUser(String username) {
        return recipeRepository.findAllByAuthor_Username(username);
    }

    @Override
    public Optional<Recipe> findByName(String name) {
        return recipeRepository.findByTitle(name);
    }

    @Transactional
    @Override
    public Optional<Recipe> save(RecipeDTO recipeDTO, List<String> imagesNames) throws IOException {
        //treba da se kreira celosen objekt od Recipe i da se zachuva!
        Recipe recipeToAdd = new Recipe();
        recipeToAdd.setStatus(RecipeStatus.PENDING);

        Cuisine cuisine = cuisineRepository.getById(recipeDTO.getCuisine());
        Meal meal = mealRepository.getById(recipeDTO.getMeal());
        recipeToAdd.setCuisine(cuisine);
        recipeToAdd.setMeal(meal);

        RecipeAuthor author = (RecipeAuthor) userService.loadUserByUsername(recipeDTO.getAuthor());
        recipeToAdd.setAuthor(author);

        recipeToAdd.setTitle(recipeDTO.getTitle());
        recipeToAdd.setDescription(recipeDTO.getDescription());

        recipeToAdd.setPrepInMins(recipeDTO.getPrepH() * 60 + recipeDTO.getPrepM());
        recipeToAdd.setCookInMins(recipeDTO.getCookH() * 60 + recipeDTO.getCookM());
        recipeToAdd.setDateCreated(LocalDateTime.now());


        //M:N
        List<Ingredient> ingredients= createMNrelations(recipeDTO,recipeToAdd);
        ingredientRepository.saveAll(ingredients);
        recipeToAdd.setIngredients(ingredients);
        Recipe addedRecipe = recipeRepository.save(recipeToAdd);

        //One to many
        List<Instruction> instructionsToAdd = getInstructions(recipeDTO,addedRecipe);
        instructionRepository.saveAll(instructionsToAdd);

        addedRecipe.setInstructions(instructionsToAdd);

        //images
        List<Image> imagesToAdd = new ArrayList<>();
        for (String m : imagesNames) {
            imagesToAdd.add(new Image(m, addedRecipe));
        }
        imageRepository.saveAll(imagesToAdd);
        addedRecipe.setImages(imagesToAdd);

        return Optional.of(recipeRepository.save(addedRecipe));

    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public Optional<Recipe> editRecipe(RecipeDTO recipeDTO, Long recipeId) {
        //za post akcijata!!!
        Recipe recipeToAdd = this.findById(recipeId);
        recipeToAdd.setStatus(RecipeStatus.PENDING);
        Cuisine cuisine = cuisineRepository.getById(recipeDTO.getCuisine());
        Meal meal = mealRepository.getById(recipeDTO.getMeal());
        recipeToAdd.setCuisine(cuisine);
        recipeToAdd.setMeal(meal);

        recipeToAdd.setTitle(recipeDTO.getTitle());
        recipeToAdd.setDescription(recipeDTO.getDescription());
        recipeToAdd.setPrepInMins(recipeDTO.getPrepH() * 60 + recipeDTO.getPrepM());
        recipeToAdd.setCookInMins(recipeDTO.getCookH() * 60 + recipeDTO.getCookM());


        //M:N
        List<Ingredient> ingredients=createMNrelations(recipeDTO,recipeToAdd);
        List<Ingredient> tmp = recipeToAdd.getIngredients();
        ingredientRepository.deleteAll(tmp);

        ingredientRepository.saveAll(ingredients);
        recipeToAdd.setIngredients(ingredients);

        Recipe addedRecipe = recipeRepository.save(recipeToAdd);

        //One to many
        List<Instruction> instructionsToAdd =getInstructions(recipeDTO,addedRecipe);
        List<Instruction> tmp2 = recipeToAdd.getInstructions();
        instructionRepository.deleteAll(tmp2);
        instructionRepository.saveAll(instructionsToAdd);
        addedRecipe.setInstructions(instructionsToAdd);

        recipeRepository.save(addedRecipe);
        return Optional.of(addedRecipe);

    }

    @Override
    public List<Recipe> findPendingPapers() {
        return recipeRepository.findAllByStatus(RecipeStatus.PENDING);
    }

    @Transactional
    @Override
    public boolean addRecipeToSaved(Long recipeId, String username) {
        Recipe toSave = this.findById(recipeId);
        RecipeAuthor author = userRepository.findByUsername(username).orElseThrow();
        author.getSavedRecipes().removeIf(r -> r.equals(toSave));
        author.getSavedRecipes().add(toSave);
        userRepository.save(author);
        recipeRepository.save(toSave);
        return true;
    }

    @Transactional
    @Override
    public boolean removeFromSaved(Long recipeId, String username) {
        Recipe toRemove = this.findById(recipeId);
        RecipeAuthor author = (RecipeAuthor) userService.loadUserByUsername(username);
        author.getSavedRecipes().remove(toRemove);
        userRepository.save(author);
        recipeRepository.save(toRemove);
        return true;
    }

    private List<Ingredient> createMNrelations(RecipeDTO recipeDTO, Recipe recipeToAdd){
        List<Tag> tags = new ArrayList<>();
        List<String> tagsList = List.of(recipeDTO.getTagList().trim().split("\\s+"));
        tagsList.stream().forEach(t -> {
            //prebaraj dali postoi takov tag, ako da asociraj go, ako ne kreiraj nov i zachuvaj vo baza
            Tag found = tagRepository.findByName(t).orElseGet(() -> tagRepository.save(new Tag(t)));
            tags.add(found);
        });
        recipeToAdd.setTagList(tags);

        //M:N
        List<SpecialConsideration> consTmp = specialConsiderationRepository
                .findAllById(recipeDTO.getConsiderations());
        recipeToAdd.setConsiderations(consTmp);

        //M:N
        List<Ingredient> ingredients = new ArrayList<>();
        List<String> names = recipeDTO.getIngredientNames();
        List<Double> quantities = recipeDTO.getIngredientQuantities();
        List<Measurement> measurements = recipeDTO.getIngredientMeasurements();
        for (int i = 0; i < names.size(); i++) {
            ingredients.add(new Ingredient(names.get(i), quantities.get(i), measurements.get(i)));
        }
        return ingredients;
    }

    private List<Instruction> getInstructions(RecipeDTO recipeDTO, Recipe addedRecipe){

       return recipeDTO.getInstructions().stream().map(i ->new Instruction(i,addedRecipe)).collect(Collectors.toList());
    }
}
