package com.ZakTrak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*    ;
import com.ZakTrak.service.UserService;
import com.ZakTrak.dto.UserInfo;
import com.ZakTrak.model.User;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class ProtectedController {
    private final UserService userService;

    @GetMapping("/")
    public UserInfo getUserInfo() {
        User user = userService.getCurrentUser();
        return new UserInfo(user.getEmail());
    }
}