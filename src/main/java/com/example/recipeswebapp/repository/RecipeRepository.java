package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    Optional<Recipe> findByTitle(String title);

    @Query(value = "SELECT * FROM Recipe WHERE status='PENDING' ORDER BY date_created desc LIMIT 8",nativeQuery = true)
    List<Recipe> findMostRecentRecipes();
    List<Recipe> findAllByAuthor_Username(String username);

}
