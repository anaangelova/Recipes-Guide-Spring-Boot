package com.example.recipeswebapp.service.implementation;

import com.example.recipeswebapp.model.*;
import com.example.recipeswebapp.model.DTO.RecipeDTO;
import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import com.example.recipeswebapp.repository.*;
import com.example.recipeswebapp.service.interfaces.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Paper;
import java.io.File;
import java.io.FileOutputStream;
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
    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository, CuisineRepository cuisineRepository, MealRepository mealRepository, IngredientRepository ingredientRepository, SpecialConsiderationRepository specialConsiderationRepository, InstructionRepository instructionRepository, TagRepository tagRepository, ImageRepository imageRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.cuisineRepository = cuisineRepository;
        this.mealRepository = mealRepository;

        this.ingredientRepository = ingredientRepository;
        this.specialConsiderationRepository = specialConsiderationRepository;
        this.instructionRepository = instructionRepository;
        this.tagRepository = tagRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public List<Recipe> findAllRecipesApprovedAndMostRecent() {
        return recipeRepository.findMostRecentRecipes();

    }

    @Override
    public List<Recipe> findAllRecipesApprovedAndTrending() {
        List<Recipe> all=recipeRepository.findAllByStatus(RecipeStatus.APPROVED);
        Collections.shuffle(all);
        if(all.size()>4)
            return all.subList(0,4);
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
    public Optional<Recipe> save(RecipeDTO recipeDTO, List<MultipartFile> images, List<String> imagesNames) throws IOException {
        //treba da se kreira celosen objekt od Recipe i da se zachuva!
        Recipe recipeToAdd=new Recipe();
        recipeToAdd.setCuisine(recipeDTO.getCuisine());
        recipeToAdd.setMeal(recipeDTO.getMeal());
        Optional<RecipeAuthor> author= userRepository.findByUsername(recipeDTO.getAuthor());
        recipeToAdd.setAuthor(author.orElseThrow());
        recipeToAdd.setTitle(recipeDTO.getTitle());
        recipeToAdd.setDescription(recipeDTO.getDescription());
        recipeToAdd.setStatus(RecipeStatus.PENDING);
        recipeToAdd.setPrepInMins(recipeDTO.getPrepH()*60+recipeDTO.getPrepM());
        recipeToAdd.setCookInMins(recipeDTO.getCookH()*60+recipeDTO.getCookM());
        recipeToAdd.setDateCreated(LocalDateTime.now());

        //M:N
        List<Tag> tags=new ArrayList<>();
        List<String> tagsList= List.of(recipeDTO.getTagList().split("\\s+"));
        for(String t: tagsList){
            //prebaraj dali postoi takov tag, ako da asociraj go, ako ne kreiraj nov i zachuvaj vo baza
            Tag found= tagRepository.findByName(t).orElseGet( () -> tagRepository.save(new Tag(t)));
            tags.add(found);
        }
       // tagRepository.saveAll(tags);
        recipeToAdd.setTagList(tags);

        //M:N
        List<SpecialConsideration> consTmp= specialConsiderationRepository
                .findAllById(recipeDTO.getConsiderations()
                        .stream().map(c -> c.getId()).collect(Collectors.toList()));
        recipeToAdd.setConsiderations(consTmp);

        //M:N
        List<Ingredient> ingredients=new ArrayList<>();
        List<String> names=recipeDTO.getIngredientNames();
        List<Double> quantities=recipeDTO.getIngredientQuantities();
        List<Measurement> measurements=recipeDTO.getIngredientMeasurements();
        for(int i=0;i<names.size();i++){
            ingredients.add(new Ingredient(names.get(i),quantities.get(i),measurements.get(i)));
        }
        ingredientRepository.saveAll(ingredients);
        recipeToAdd.setIngredients(ingredients);


        Recipe addedRecipe= recipeRepository.save(recipeToAdd);

        //One to many
        List<Instruction> instructionsToAdd=new ArrayList<>();
        for(String i: recipeDTO.getInstructions()){
            instructionsToAdd.add(new Instruction(i,addedRecipe));
        }
        instructionRepository.saveAll(instructionsToAdd);
        addedRecipe.setInstructions(instructionsToAdd);

        //images

        List<Image> imagesToAdd=new ArrayList<>();
        for(String m: imagesNames){
            imagesToAdd.add(new Image(m,addedRecipe));
        }
        imageRepository.saveAll(imagesToAdd);
        addedRecipe.setImages(imagesToAdd);

        recipeRepository.save(addedRecipe);
        return Optional.of(addedRecipe);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public Optional<Recipe> editRecipe(RecipeDTO recipeDTO, Long recipeId) {
        //za post akcijata!!!
        Recipe recipeToAdd=this.findById(recipeId).orElseThrow();
        recipeToAdd.setCuisine(recipeDTO.getCuisine());
        recipeToAdd.setMeal(recipeDTO.getMeal());
        recipeToAdd.setTitle(recipeDTO.getTitle());
        recipeToAdd.setDescription(recipeDTO.getDescription());
        recipeToAdd.setStatus(RecipeStatus.PENDING);
        recipeToAdd.setPrepInMins(recipeDTO.getPrepH()*60+recipeDTO.getPrepM());
        recipeToAdd.setCookInMins(recipeDTO.getCookH()*60+recipeDTO.getCookM());


        //M:N
        List<Tag> tags=new ArrayList<>();
        List<String> tagsList= List.of(recipeDTO.getTagList().split("\\s+"));
        for(String t: tagsList){
            //prebaraj dali postoi takov tag, ako da asociraj go, ako ne kreiraj nov i zachuvaj vo baza
            Tag found= tagRepository.findByName(t).orElseGet( () -> tagRepository.save(new Tag(t)));
            tags.add(found);
        }
        // tagRepository.saveAll(tags);
        recipeToAdd.setTagList(tags);

        //M:N
        List<SpecialConsideration> consTmp= specialConsiderationRepository
                .findAllById(recipeDTO.getConsiderations()
                        .stream().map(c -> c.getId()).collect(Collectors.toList()));

        recipeToAdd.setConsiderations(consTmp);

        //M:N
        List<Ingredient> ingredients=new ArrayList<>();
        List<String> names=recipeDTO.getIngredientNames();
        List<Double> quantities=recipeDTO.getIngredientQuantities();
        List<Measurement> measurements=recipeDTO.getIngredientMeasurements();
        for(int i=0;i<names.size();i++){
            ingredients.add(new Ingredient(names.get(i),quantities.get(i),measurements.get(i)));
        }
        List<Ingredient> tmp=recipeToAdd.getIngredients();
        ingredientRepository.deleteAll(tmp); //???

        ingredientRepository.saveAll(ingredients);
        recipeToAdd.setIngredients(ingredients);


        Recipe addedRecipe= recipeRepository.save(recipeToAdd);

        //One to many
        List<Instruction> instructionsToAdd=new ArrayList<>();
        for(String i: recipeDTO.getInstructions()){
            instructionsToAdd.add(new Instruction(i,addedRecipe));
        }

        List<Instruction> tmp2=recipeToAdd.getInstructions();
        instructionRepository.deleteAll(tmp2); //???

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
        Recipe toSave=this.findById(recipeId).orElseThrow();
        RecipeAuthor author=userRepository.findByUsername(username).orElseThrow();
        author.getSavedRecipes().removeIf(r -> r.equals(toSave));
        author.getSavedRecipes().add(toSave);
        userRepository.save(author);
        recipeRepository.save(toSave); //do I need it?
        return true;
    }

    @Transactional
    @Override
    public boolean removeFromSaved(Long recipeId, String username) {
        Recipe toRemove=this.findById(recipeId).orElseThrow();
        RecipeAuthor author=userRepository.findByUsername(username).orElseThrow();
        author.getSavedRecipes().remove(toRemove);
        userRepository.save(author);
        recipeRepository.save(toRemove); //do I need it?
        return true;
    }
}
