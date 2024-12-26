package com.ZakTrak.controller;

import com.ZakTrak.dto.AuthenticationRequest;
import com.ZakTrak.dto.AuthenticationResponse;
import com.ZakTrak.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthenitcationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void shouldAuthenticateValidUser(){
        //Arrange
        String email = "test@example.com";
        String password = "Password123";
        String token = "jwt.token.here";

        AuthenticationRequest request = new AuthenticationRequest(email, password);
        AuthenticationResponse expectedResponse = new AuthenticationResponse(token);

        when(authenticationService.authenticate(request)).thenReturn(expectedResponse);

        //Act
        AuthenticationResponse response = authenticationController.authenticate(request);

        //Assert
        assertThat(response).isEqualTo(expectedResponse);
        assertThat(response.token()).isEqualTo(token);

    }

    @Test
    void shouldLoginUserAndReturnToken() {
        // Arrange
        String email = "test@example.com";
        String password = "Password123";
        String expectedToken = "jwt.token.here";

        AuthenticationRequest loginRequest = new AuthenticationRequest(email, password);
        AuthenticationResponse expectedResponse = new AuthenticationResponse(expectedToken);

        when(authenticationService.authenticate(loginRequest))
                .thenReturn(expectedResponse);

        // Act
        AuthenticationResponse actualResponse =
                authenticationController.login(loginRequest);

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.token()).isEqualTo(expectedToken);
    }

}
