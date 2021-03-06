package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional<Tag> findByName(String name);
    Optional<Tag> findByNameIgnoreCase(String name);
}
