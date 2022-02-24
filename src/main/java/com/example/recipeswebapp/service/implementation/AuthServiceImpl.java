package com.example.recipeswebapp.service.implementation;

import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import com.example.recipeswebapp.model.exceptions.InvalidUserCredentialsException;
import com.example.recipeswebapp.repository.UserRepository;
import com.example.recipeswebapp.service.interfaces.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RecipeAuthor login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException();
        return userRepository.findByUsernameAndPassword(username, password).orElseThrow(InvalidUserCredentialsException::new);

    }
}
