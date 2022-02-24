package com.example.recipeswebapp.model.DTO;

import com.example.recipeswebapp.model.Recipe;
import lombok.Data;

@Data
public class EditDTO {
    private Recipe recipe;
    private String keywords;
    private int prepH;
    private int cookH;

    public EditDTO(Recipe recipe, String keywords, int prepH, int cookH) {
        this.recipe = recipe;
        this.keywords = keywords;
        this.prepH = prepH;
        this.cookH = cookH;
    }
}
