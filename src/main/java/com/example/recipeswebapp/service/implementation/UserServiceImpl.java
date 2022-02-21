package com.example.recipeswebapp.service.implementation;

import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import com.example.recipeswebapp.model.Identity.Role;
import com.example.recipeswebapp.repository.UserRepository;
import com.example.recipeswebapp.service.interfaces.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RecipeAuthor register(String username, String email, String password, String repeatPassword, String firstName, String lastName, Role role) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException();
        if (password.equals(repeatPassword)) {
            RecipeAuthor user = new RecipeAuthor(username,email, passwordEncoder.encode(password), firstName, lastName,role);
            //ako vekje ima takov user??? spored biznis logikata ne treba da go izbrisheme prethodniot korisnik! tuku ednostavno
            //ne treba da dozvolime da se vnese nov korisnik voopshto!
            if (userRepository.findByUsername(username).isPresent()) {
                throw new BadCredentialsException("Username already exists!"); //mozhe so custom exception!!
            }

            return userRepository.save(user);
        } else throw new BadCredentialsException("Passwords do not match!"); //mozhe so custom exception!!
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
    }
}
