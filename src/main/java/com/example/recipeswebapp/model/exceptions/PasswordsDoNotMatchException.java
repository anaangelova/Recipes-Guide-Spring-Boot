package com.example.recipeswebapp.model.exceptions;

public class PasswordsDoNotMatchException extends RuntimeException{
    public PasswordsDoNotMatchException() {

        super("Passwords do not match!");
    }
}
