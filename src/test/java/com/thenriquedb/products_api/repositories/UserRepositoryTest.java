package com.thenriquedb.products_api.repositories;

import com.thenriquedb.products_api.domain.User;
import com.thenriquedb.products_api.domain.UserRole;
import com.thenriquedb.products_api.modules.auth.dtos.RegisterRecordDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should return a user by login")
    public void shouldReturnUserByLogin() {
        String login = "usertest";
        createUser(new RegisterRecordDto(login, "password", "name", UserRole.ADMIN));
        UserDetails result = userRepository.findByLogin(login);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should return null when user not found")
    public void shouldReturnNullWhenUserNotFound() {
        UserDetails result = userRepository.findByLogin("notfound");

        assertThat(result).isNull();
    }

    private User createUser(RegisterRecordDto registerRecordDto) {
        User newUser = new User(
                registerRecordDto.name(),
                registerRecordDto.login(),
                registerRecordDto.password(),
                registerRecordDto.role()
        );

        entityManager.persist(newUser);

        return newUser;
    }
}