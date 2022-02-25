package com.example.recipeswebapp.model.exceptions;

public class SubscriberExistsException extends RuntimeException{

    public SubscriberExistsException(String email){
        super(String.format("Subscriber with email address %s already exists!",email));
    }
}
