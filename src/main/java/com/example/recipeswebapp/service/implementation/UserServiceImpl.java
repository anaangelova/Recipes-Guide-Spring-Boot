package com.example.recipeswebapp.service.implementation;

import com.example.recipeswebapp.config.CustomOAuth2User;
import com.example.recipeswebapp.model.Identity.Provider;
import com.example.recipeswebapp.model.Identity.RecipeAuthor;
import com.example.recipeswebapp.model.Identity.Role;
import com.example.recipeswebapp.model.exceptions.PasswordValidationFailedException;
import com.example.recipeswebapp.model.exceptions.PasswordsDoNotMatchException;
import com.example.recipeswebapp.model.exceptions.UserExistsException;
import com.example.recipeswebapp.repository.UserRepository;
import com.example.recipeswebapp.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if(passwordValidation(password)){
                RecipeAuthor user = new RecipeAuthor(username,email, passwordEncoder.encode(password), firstName, lastName,role,Provider.LOCAL);
                if (userRepository.findByUsername(username).isPresent()) {
                    throw new UserExistsException(username);
                }

                return userRepository.save(user);
            }else throw new PasswordValidationFailedException();

        } else throw new PasswordsDoNotMatchException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
    }

    @Override
    public void processOAuthPostLogin(CustomOAuth2User oauthUser) {

        Optional<RecipeAuthor> existUser = userRepository.findByEmail(oauthUser.getEmail());
        String clientName=oauthUser.getOauth2ClientName().toLowerCase();
        if (existUser.isEmpty()) {
            RecipeAuthor newUser = new RecipeAuthor();
            newUser.setRole(Role.ROLE_USER);
            newUser.setUsername(oauthUser.getName());
            newUser.setEmail(oauthUser.getEmail());
            if(clientName.equals("google"))
                newUser.setProvider(Provider.GOOGLE);
            else newUser.setProvider(Provider.FACEBOOK);
            newUser.setEnabled(true);

            userRepository.save(newUser);
        }

    }
    private boolean passwordValidation(String password)
    {

        if(password.length()>=8)
        {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();

        }
        else return false;

    }
}
