package com.example.recipeswebapp.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException{

    public RecipeNotFoundException(Long id) {
        super(String.format("Recipe with id: %d was not found", id));
    }

}
