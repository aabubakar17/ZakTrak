package com.ZakTrak.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import com.ZakTrak.model.User;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ZakTrak.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldCreateNewUser() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "Password123";
        String encodedPassword = "encodedPassword123";
        User user = new User(email, encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        User createdUser = userService.createUser(email, rawPassword);

        // Assert
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(email);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        // Arrange
        String email = "test@example.com";
        String password = "Password123";

        when(userRepository.findByEmail(email)).thenReturn(new User(email, password));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(email, password), "Email already exists");

        // Verify exception message
        assertThat(exception.getMessage()).isEqualTo("Email already exists");
    }

    @Test
    void shouldEncryptPasswordWhenCreatingUser() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "Password123";
        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.findByEmail(email)).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User createdUser = userService.createUser(email, rawPassword);

        // Assert
        verify(passwordEncoder).encode(rawPassword);
        assertThat(createdUser.getPassword()).isEqualTo(encodedPassword);
    }
}
