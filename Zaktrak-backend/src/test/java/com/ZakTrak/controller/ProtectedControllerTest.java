package com.ZakTrak.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ZakTrak.model.User;
import com.ZakTrak.service.UserService;
import com.ZakTrak.dto.UserInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProtectedControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ProtectedController protectedController;

    @Test
    void shouldReturnUserDetailsForAuthenticatedUser() {
        // Arrange
        String email = "test@example.com";
        String password = "Password123";
        String firstName = "Test";
        String lastName = "User";
        User user = new User(email, password, firstName, lastName);
        when(userService.getCurrentUser()).thenReturn(user);

        // Act
        UserInfo response = protectedController.getUserInfo();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(email);
    }

}
