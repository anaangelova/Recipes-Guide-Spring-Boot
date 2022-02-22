package com.example.recipeswebapp.model.DTO;

import com.example.recipeswebapp.model.*;
import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RecipeDTO {
    @NotNull
    private String title;
    @NotNull
    private String description;

    private String author;

    @NotNull
    private List<String> instructions;

    @NotNull
    private Meal meal;

    @NotNull
    private Cuisine cuisine;


    @NotNull
    private String tagList;


   @NotEmpty
    private List<SpecialConsideration> considerations;

    @NotNull
    private List<String> ingredientNames;
    @NotNull
    private List<Double> ingredientQuantities;
    @NotNull
    private List<Measurement> ingredientMeasurements;

    @NotNull
    private Double prepH;
    @NotNull
    private Double prepM;
    @NotNull
    private Double cookH;
    @NotNull
    private Double cookM;
}
