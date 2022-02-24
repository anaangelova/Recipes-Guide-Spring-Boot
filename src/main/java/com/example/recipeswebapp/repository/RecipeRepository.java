package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.Recipe;
import com.example.recipeswebapp.model.RecipeStatus;
import com.example.recipeswebapp.model.SpecialConsideration;
import com.example.recipeswebapp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    Optional<Recipe> findByTitle(String title);

    @Query(value = "SELECT * FROM Recipe WHERE status='APPROVED' ORDER BY date_created desc LIMIT 4",nativeQuery = true)
    List<Recipe> findMostRecentRecipes();
    List<Recipe> findAllByAuthor_Username(String username);
    List<Recipe> findAllByStatus(RecipeStatus status);

    List<Recipe> findAllByTitleContainingIgnoreCaseAndStatus(String title,RecipeStatus status);
    List<Recipe> findAllByMeal_NameContainingIgnoreCaseAndStatus(String name, RecipeStatus status);
    List<Recipe> findAllByCuisine_NameContainingIgnoreCaseAndStatus(String name,RecipeStatus status);

    List<Recipe> findAllByTagListContains(Tag tag);
    List<Recipe> findAllByConsiderationsContainsAndStatus(SpecialConsideration consideration, RecipeStatus status);



}
