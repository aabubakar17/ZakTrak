package com.ZakTrak.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveandFindUserByEmail() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User(email, password);

        // Act
        userRepository.save(user);
        user foundUser = userRepository.findByEmail(email);

        // Assert
        assertEquals(email, foundUser.getEmail(), "Email should match the provided value");
        assertThat(foundUser).isNotNull();
    }
}
