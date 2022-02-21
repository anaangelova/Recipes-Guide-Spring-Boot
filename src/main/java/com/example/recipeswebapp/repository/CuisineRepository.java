package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepository extends JpaRepository<Cuisine,Long> {
}
