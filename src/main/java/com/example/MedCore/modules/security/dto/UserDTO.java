package com.example.MedCore.modules.security.dto;

import lombok.Getter;

public record UserDTO(
        String login,
        String password,
        String email,
        String phone,
        String status
) {
    public UserDTO {
        // Можно добавить проверки, если нужно (например, на валидность email, phone и т.д.)
    }
}
