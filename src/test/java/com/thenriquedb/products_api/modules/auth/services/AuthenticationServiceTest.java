package com.thenriquedb.products_api.modules.auth.services;

import com.thenriquedb.products_api.domain.User;
import com.thenriquedb.products_api.domain.UserRole;
import com.thenriquedb.products_api.infra.security.TokenService;
import com.thenriquedb.products_api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
class AuthenticationServiceTest {
    private final String login = "username";
    private final String password = "password";
    private final String encryptedPassword = "encryptedPassword";
    private final String name = "John Doe";
    private final UserRole role = UserRole.ADMIN;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenService tokenService;

    @InjectMocks
    AuthenticationService authenticationService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        when(passwordEncoder.encode(password)).thenReturn(encryptedPassword);
    }

    @Test
    @DisplayName("Should create a new user")
    void shouldRegisterUser() {
        User user = new User(name, login, encryptedPassword, role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = authenticationService.register(name, login, password, UserRole.ADMIN);
        when(userRepository.findByLogin(login)).thenReturn(createdUser);

        UserDetails result = userRepository.findByLogin(login);
        assertThat(result).isNotNull();
    }
}