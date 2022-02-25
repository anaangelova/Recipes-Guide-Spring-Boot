package com.example.recipeswebapp.model.exceptions;

public class PasswordValidationFailedException extends RuntimeException{

    public PasswordValidationFailedException(){
        super("Password length should be at least 8 characters and password should contain at least one of each: lowercase letter, uppercase letter, digit and special character!");
    }
}
