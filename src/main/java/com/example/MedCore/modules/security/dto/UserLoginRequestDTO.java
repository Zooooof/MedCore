package com.example.MedCore.modules.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginRequestDTO {
    @NotBlank(message = "Login or Email cannot be null or empty")
    private String loginOrEmail;
    private String password;
}
