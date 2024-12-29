package com.example.MedCore.modules.security.securityUser.Validators;

import com.example.MedCore.modules.security.dto.UserLoginRequestDTO;
import com.example.MedCore.modules.security.dto.UserRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidatorUser {
    public void validateRegisterRequest(UserRequestDTO requestDTO) {
        if (requestDTO.getLogin() == null || requestDTO.getLogin().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }
        if (requestDTO.getPassword() == null || requestDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (requestDTO.getEmail() == null || requestDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (requestDTO.getPhone() == null || requestDTO.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        if (requestDTO.getStatus() == null || requestDTO.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (requestDTO.getDocument_id() == null) {
            throw new IllegalArgumentException("Document ID cannot be null");
        }
    }

    public void validateLoginRequest(UserLoginRequestDTO requestDTO) {
        // Валидация логина или email
        if (requestDTO.getLoginOrEmail() == null || requestDTO.getLoginOrEmail().isEmpty()) {
            throw new IllegalArgumentException("Login or Email cannot be null or empty");
        }
        if (requestDTO.getPassword() == null || requestDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}
