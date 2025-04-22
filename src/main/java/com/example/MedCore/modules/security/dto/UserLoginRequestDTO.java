package com.example.MedCore.modules.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record UserLoginRequestDTO(
        @NotBlank(message = "Login or Email cannot be null or empty") String loginOrEmail,
        String password
) {}