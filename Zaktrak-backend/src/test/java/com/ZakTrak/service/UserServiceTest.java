package com.ZakTrak.service;


import com.ZakTrak.dto.AuthenticationResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import com.ZakTrak.dto.NewUserRequest;
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

    @Mock
    private JwtService jwtService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

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
        String firstName = "Test";
        String lastName = "User";

        NewUserRequest request = new NewUserRequest(email, rawPassword, firstName, lastName);
        User user = new User(request.email(), request.password(), request.firstName(), request.lastName());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(jwtService.generateToken(user)).thenReturn("token");
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        AuthenticationResponse token = userService.createUser(request);

        // Assert
        assertThat(token.token()).isEqualTo("token");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        // Arrange
        String email = "test@example.com";
        String password = "Password123";
        String firstName = "Test";
        String lastName = "User";
        NewUserRequest request = new NewUserRequest(email, password, firstName, lastName);

        when(userRepository.findByEmail(email)).thenReturn(new User(request.email(), request.password(), request.firstName(), request.lastName()));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userService.createUser(request),
                "Should throw IllegalStateException when email already exists"
        );

        // Verify exception message
        assertThat(exception.getMessage()).isEqualTo("Email already registered");
    }

    @Test
    void shouldEncryptPasswordWhenCreatingUser() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "Password123";
        String encodedPassword = "encodedPassword123";
        String firstName = "Test";
        String lastName = "User";

        NewUserRequest request = new NewUserRequest(email, rawPassword, firstName, lastName);


        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.findByEmail(email)).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        // Act
        AuthenticationResponse createdUser = userService.createUser(request);

        // Assert
        verify(passwordEncoder).encode(rawPassword);
        assertThat(createdUser.token()).isEqualTo("token");
    }

    @Test
    void shouldReturnCurrentUser() {
        // Arrange
        String email = "test@example.com";
        String password = "Password123";
        String firstName = "Test";
        String lastName = "User";
        User expectedUser = new User(email, password, firstName, lastName);


        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password("password123")
                .authorities(Collections.emptyList())
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(email)).thenReturn(expectedUser);
        SecurityContextHolder.setContext(securityContext);


        // Act
        User currentUser = userService.getCurrentUser();

        // Assert
        assertThat(currentUser).isEqualTo(expectedUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String email = "test@example.com";
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password("password123")
                .authorities(Collections.emptyList())
                .build();
        // Set up the security context properly
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        // Mock the repository to return null (user not found)
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.getCurrentUser(),
                "Should throw UsernameNotFoundException when user is not found"
        );

        // Verify the exception message
        assertThat(exception.getMessage()).contains(email);

    }
}
