package com.example.recipeswebapp.repository;

import com.example.recipeswebapp.model.SpecialConsideration;
import com.example.recipeswebapp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialConsiderationRepository extends JpaRepository<SpecialConsideration,Long> {

    Optional<SpecialConsideration> findByNameIgnoreCase(String name);
}
