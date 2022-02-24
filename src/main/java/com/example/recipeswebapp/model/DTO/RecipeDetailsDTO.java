package com.example.recipeswebapp.model.DTO;

import com.example.recipeswebapp.model.Recipe;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RecipeDetailsDTO {
    private Set<String> allTags;
    private List<String> allInstr;
    private List<String> allIngr;
    private String formatted;
    private Recipe recipe;

    public RecipeDetailsDTO(Set<String> allTags, List<String> allInstr, List<String> allIngr, String formatted, Recipe recipe) {
        this.allTags = allTags;
        this.allInstr = allInstr;
        this.allIngr = allIngr;
        this.formatted = formatted;
        this.recipe = recipe;
    }
}
