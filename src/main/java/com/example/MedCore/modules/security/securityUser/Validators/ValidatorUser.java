package com.example.MedCore.modules.security.securityUser.Validators;

import com.example.MedCore.modules.security.dto.UserLoginRequestDTO;
import com.example.MedCore.modules.security.dto.UserRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidatorUser {
    public void validateRegisterRequest(UserRequestDTO requestDTO) {
        if (requestDTO.login() == null || requestDTO.login().isEmpty()) {
            throw new IllegalArgumentException("Логин не может быть нулевым или пустым");
        }
        if (requestDTO.password() == null || requestDTO.password().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть нулевым или пустым");
        }
        if (requestDTO.email() == null || requestDTO.email().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть нулевым или пустым");
        }
        if (requestDTO.phone() == null || requestDTO.phone().isEmpty()) {
            throw new IllegalArgumentException("Номер телефона не может быть нулевым или пустым");
        }
        if (requestDTO.status() == null || requestDTO.status().isEmpty()) {
            throw new IllegalArgumentException("Статус не может быть нулевым или пустым");
        }
        if (requestDTO.document_id() == null) {
            throw new IllegalArgumentException("Айди документа не может быть нулевым или пустым");
        }
    }

    public void validateLoginRequest(UserLoginRequestDTO requestDTO) {
        // Валидация логина или email
        if (requestDTO.loginOrEmail() == null || requestDTO.loginOrEmail().isEmpty()) {
            throw new IllegalArgumentException("Логин или адрес электронной почты не могут быть пустыми");
        }
        if (requestDTO.password() == null || requestDTO.password().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть нулевым или незаполненным");
        }
    }
}
