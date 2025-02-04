package com.thenriquedb.products_api.controllers;

import com.thenriquedb.products_api.dtos.AuthenticationRecordDto;
import com.thenriquedb.products_api.dtos.RegisterRecordDto;
import com.thenriquedb.products_api.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRecordDto authenticationRecordDto) {
        authenticationService.login(
                authenticationRecordDto.login(),
                authenticationRecordDto.password()
        );

        return ResponseEntity.ok().build();
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
