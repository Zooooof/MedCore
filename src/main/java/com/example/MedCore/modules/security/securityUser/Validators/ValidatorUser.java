package com.example.MedCore.modules.security.securityUser.Validators;

import com.example.MedCore.modules.security.dto.UserLoginRequestDTO;
import com.example.MedCore.modules.security.dto.UserRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidatorUser {
    public void validateRegisterRequest(UserRequestDTO requestDTO) {
        if (requestDTO.login() == null || requestDTO.login().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }
        if (requestDTO.password() == null || requestDTO.password().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (requestDTO.email() == null || requestDTO.email().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (requestDTO.phone() == null || requestDTO.phone().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        if (requestDTO.status() == null || requestDTO.status().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (requestDTO.document_id() == null) {
            throw new IllegalArgumentException("Document ID cannot be null");
        }
    }

    public void validateLoginRequest(UserLoginRequestDTO requestDTO) {
        // Валидация логина или email
        if (requestDTO.loginOrEmail() == null || requestDTO.loginOrEmail().isEmpty()) {
            throw new IllegalArgumentException("Login or Email cannot be null or empty");
        }
        if (requestDTO.password() == null || requestDTO.password().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}
