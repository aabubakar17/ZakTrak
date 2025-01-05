package com.ZakTrak.repository;
import com.ZakTrak.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    
    @Test
    void shouldSaveandFindUserByEmail() {
        // Arrange
        String email = "test@example.com";
        String password = "Password123";
        String firstName = "Test";
        String lastName = "User";
        User user = new User(email, password, firstName, lastName);

        // Act
        userRepository.save(user);
        User foundUser = userRepository.findByEmail(email);

        // Assert
        assertEquals(email, foundUser.getEmail(), "Email should match the provided value");
        assertEquals(password, foundUser.getPassword(), "Password should match the provided value");
        assertEquals(firstName, foundUser.getFirstName(), "First name should match the provided value");
        assertEquals(lastName, foundUser.getLastName(), "Last name should match the provided value");
        assertThat(foundUser).isNotNull();
    }

}
