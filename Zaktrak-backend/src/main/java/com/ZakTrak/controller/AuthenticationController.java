package com.ZakTrak.controller;


import com.ZakTrak.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import com.ZakTrak.dto.AuthenticationRequest;
import com.ZakTrak.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    AuthenticationResponse authenticate(AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

}
