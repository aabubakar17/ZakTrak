package com.ZakTrak.controller;

import com.ZakTrak.model.User;
import org.springframework.stereotype.Controller;
import com.ZakTrak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ZakTrak.dto.NewUserRequest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.ZakTrak.dto.AuthenticationResponse;


@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/api/user")
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse registerUser(@RequestBody  NewUserRequest request) {
        return userService.createUser(request);
    }


}
