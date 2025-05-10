package com.example.MedCore.modules.security.dto;

import lombok.Getter;

public record UserDTO(
        String login,
        String password,
        String email,
        String phone,
        String status
) {}
