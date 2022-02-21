package com.example.recipeswebapp.model;


import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToMany
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private List<Instruction> instructions;

    @ManyToOne
    private Meal meal;
    @ManyToOne
    private Cuisine cuisine;

    @ManyToOne
    private RecipeAuthor author;

    @OneToMany(mappedBy = "recipeOwner")
    private List<Image> images;

    @ManyToMany
    private List<Tag> tagList;

    @ManyToMany
    private List<SpecialConsideration> considerations;

    @Enumerated(value = EnumType.STRING)
    private RecipeStatus status;



}
