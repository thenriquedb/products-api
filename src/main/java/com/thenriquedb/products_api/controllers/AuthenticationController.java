package com.thenriquedb.products_api.controllers;

import com.thenriquedb.products_api.dtos.AuthenticationRecordDto;
import com.thenriquedb.products_api.dtos.RegisterRecordDto;
import com.thenriquedb.products_api.execptions.UserAlreadyExistsException;
import com.thenriquedb.products_api.models.UserModel;
import com.thenriquedb.products_api.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRecordDto authenticationRecordDto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationRecordDto.login(), authenticationRecordDto.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRecordDto registerRecordDto) {
        UserDetails user = userRepository.findByLogin(registerRecordDto.login());

        if (user != null) {
            throw new UserAlreadyExistsException(registerRecordDto.login());
        }

        String encryptedPassword = passwordEncoder.encode(registerRecordDto.password());
        var newUser = new UserModel(
                registerRecordDto.name(),
                registerRecordDto.login(),
                encryptedPassword,
                registerRecordDto.role()
        );

        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
