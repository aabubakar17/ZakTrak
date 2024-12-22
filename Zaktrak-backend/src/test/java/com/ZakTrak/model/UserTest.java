package com.ZakTrak.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void userShouldHaveBasicProperties() {
        // Arrange
        String email = "test@example.com";
        String password = "Password123";

        // Act
        User user = new User(email, password);

        // Assert
        assertEquals(email, user.getEmail(), "Email should be match the provided value");
        assertEquals(password, user.getPassword(), "Password should not be null");

    }

    @Test
    void userEmailShouldNotBeNull() {
        //Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new User(null, "password"), "Email should not be null");

        //verify exception message
        assertEquals("Email should not be null", exception.getMessage(), "Exception message should be 'Email should not be null'");
    }

    @Test
    void userEmailShouldBeValidFormat() {
        //Arrange
        String invalidEmail = "notanemail";
        String password = "password";

        //Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new User(invalidEmail, password), "Email should be in valid format");

        //verify exception message
        assertEquals("Invalid format", exception.getMessage(), "Exception message should be 'Invalid format'");
    }

    @Test
    void passwordShouldMeetSecurityRequirements() {
        // Arrange
        String email = "test@example.com";
        String[] weakPasswords = {
                "short",           // too short
                "nouppercase1",    // no uppercase letter
                "NONUMBERS",       // no numbers
                "       12A"       // mostly whitespace
        };

        // Act & Assert
        for (String password : weakPasswords) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(email, password),
                    "Weak password should throw IllegalArgumentException"
            );

            // Verify that the exception message contains "Password must"
            assertTrue(
                    exception.getMessage().contains("Password must"),
                    "Exception message should explain password requirements"
            );
        }
    }


}
