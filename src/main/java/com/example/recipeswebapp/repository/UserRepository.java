package com.example.recipeswebapp.repository;


import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<RecipeAuthor,String> {
    Optional<RecipeAuthor> findByUsernameAndPassword(String username, String password);
    Optional<RecipeAuthor> findByUsername(String username);
}
