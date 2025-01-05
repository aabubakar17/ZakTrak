package com.ZakTrak.dto;

public record NewUserRequest(String email, String password, String firstName, String lastName) {
}
