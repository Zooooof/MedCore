package com.example.MedCore.modules.clinic.dto;

import com.example.MedCore.modules.clinic.entity.Clinic;
import com.example.MedCore.modules.clinic.entity.Specialization;
import com.example.MedCore.modules.security.entity.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;


import java.time.LocalDate;

public record DoctorCreateDTO(
        Long specializationId,
        Long clinicId,

        String firstname,
        String lastname,
        String surname,

        @NotBlank(message = "Логин не может быть нулевым или пустым")
        @Size(min = 8, max = 20, message = "Login must be between 8 and 20 characters")
        String login,

        @NotBlank(message = "Пароль не может быть нулевым или незаполненным")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$", message = "Пароль должен содержать по крайней мере одну цифру, одну строчную букву, одну заглавную букву, один специальный символ и не содержать пробелов")
        String password,

        @NotBlank(message = "Адрес электронной почты не может быть пустым")
        @Email(message = "Адрес электронной почты должен быть действительным")
        String email,

        @NotBlank(message = "Номер телефона не может быть нулевым или пустым")
        String phone
) {}

