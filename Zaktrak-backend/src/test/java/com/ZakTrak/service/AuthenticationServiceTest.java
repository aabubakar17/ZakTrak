package com.ZakTrak.service;

import com.ZakTrak.dto.AuthenticationRequest;
import com.ZakTrak.dto.AuthenticationResponse;
import com.ZakTrak.model.User;
import com.ZakTrak.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldAuthenticateUserAndReturnToken() {
        // Arrange
        String email = "test@example.com";
        String password = "Password123";
        String token = "generated.jwt.token";
        String firstName = "Test";
        String lastName = "User";

        User user = new User(email, password, firstName, lastName);
        AuthenticationRequest request = new AuthenticationRequest(email, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(email, password));
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(token);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        String password = "Password123";
        AuthenticationRequest request = new AuthenticationRequest(email, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new IllegalStateException("Invalid credentials"));


        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                authenticationService.authenticate(request));

    }
}