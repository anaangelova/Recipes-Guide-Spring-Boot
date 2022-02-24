package com.example.recipeswebapp.model;


import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @ToString.Exclude
    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER)
    private List<Instruction> instructions;

    @ManyToOne
    private Meal meal;
    @ManyToOne
    private Cuisine cuisine;

    @ManyToOne
    private RecipeAuthor author;

    @ToString.Exclude
    @OneToMany(mappedBy = "recipeOwner")
    private List<Image> images;

    @ManyToMany
    private List<Tag> tagList;

    @ManyToMany
    private List<SpecialConsideration> considerations;

    @Enumerated(value = EnumType.STRING)
    private RecipeStatus status;

    private Double prepInMins;
    private Double cookInMins;

    private LocalDateTime dateCreated;

}
