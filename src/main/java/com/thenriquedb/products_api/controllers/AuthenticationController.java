package com.thenriquedb.products_api.controllers;

import com.thenriquedb.products_api.dtos.AuthenticationRecordDto;
import com.thenriquedb.products_api.dtos.LoginRecordResponseDto;
import com.thenriquedb.products_api.dtos.RegisterRecordDto;
import com.thenriquedb.products_api.infra.security.TokenService;
import com.thenriquedb.products_api.domain.User;
import com.thenriquedb.products_api.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRecordDto authenticationRecordDto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationRecordDto.login(), authenticationRecordDto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginRecordResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRecordDto registerRecordDto) {
        authenticationService.register(
                registerRecordDto.name(),
                registerRecordDto.login(),
                registerRecordDto.password(),
                registerRecordDto.role()
        );

        return ResponseEntity.ok().build();
    }
}
