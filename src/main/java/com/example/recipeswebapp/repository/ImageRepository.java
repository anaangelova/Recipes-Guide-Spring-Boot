package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
