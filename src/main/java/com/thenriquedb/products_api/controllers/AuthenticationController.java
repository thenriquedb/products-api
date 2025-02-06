package com.thenriquedb.products_api.controllers;

import com.thenriquedb.products_api.dtos.AuthenticationRecordDto;
import com.thenriquedb.products_api.dtos.LoginRecordResponseDto;
import com.thenriquedb.products_api.dtos.RegisterRecordDto;
import com.thenriquedb.products_api.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;


    @PostMapping("/login")
    @Operation(summary = "Login")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Credentials invalid")
    @ApiResponse(responseCode = "403", description = "Credentials invalid")
    public ResponseEntity login(@RequestBody @Valid AuthenticationRecordDto authenticationRecordDto) {
        var token = this.authenticationService.login();
        return ResponseEntity.ok(new LoginRecordResponseDto(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User registered")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "400", description = "Invalid data")
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
