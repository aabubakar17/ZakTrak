package com.ZakTrak.controller;

import com.ZakTrak.dto.AuthenticationResponse;
import com.ZakTrak.dto.NewUserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import com.ZakTrak.service.UserService;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ZakTrak.model.User;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;




@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
    }



    @Test
    void shouldANewRegisterUser() {
        // Arrange
        String email = "test@example.com";
        String password = "Password123";
        String jwtToken = "jwt";
        String firstName = "Test";
        String lastName = "User";
        NewUserRequest request = new NewUserRequest(email, password, firstName, lastName);
        AuthenticationResponse response = new AuthenticationResponse(jwtToken);
        when(userService.createUser(request)).thenReturn(response);
        // Act
        AuthenticationResponse newToken = userController.registerUser(request);

        // Assert
        assertThat(newToken.token()).isEqualTo(jwtToken);

    }


}
