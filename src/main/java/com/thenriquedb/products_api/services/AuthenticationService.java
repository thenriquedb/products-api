package com.thenriquedb.products_api.services;


import com.thenriquedb.products_api.execptions.UserAlreadyExistsException;
import com.thenriquedb.products_api.models.UserModel;
import com.thenriquedb.products_api.models.UserRole;
import com.thenriquedb.products_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void login(String login, String password) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(login, password);
        var auth = authenticationManager.authenticate(usernamePassword);
    }

    public UserModel register(String name, String login, String password, UserRole role) {
        UserDetails user = userRepository.findByLogin(login);

        if (user != null) {
            throw new UserAlreadyExistsException(login);
        }

        String encryptedPassword = passwordEncoder.encode(password);
        var newUser = new UserModel(
                name,
                login,
                encryptedPassword,
                role
        );

       return userRepository.save(newUser);
    }
}
