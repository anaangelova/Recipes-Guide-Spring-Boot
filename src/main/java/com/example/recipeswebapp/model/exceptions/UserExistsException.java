package com.example.recipeswebapp.model.exceptions;

public class UserExistsException extends RuntimeException{

    public UserExistsException(String username) {
        super(String.format("Username %s already exists", username));
    }
}
