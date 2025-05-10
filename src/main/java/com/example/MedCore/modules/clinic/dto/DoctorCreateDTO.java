package com.example.MedCore.modules.clinic.dto;

import jakarta.validation.constraints.*;

public record DoctorCreateDTO(
        Long specializationId,
        Long clinicId,

        String firstname,
        String lastname,
        String surname,

        @NotBlank(message = "Логин не может быть нулевым или пустым")
        @Size(min = 8, max = 20, message = "Длина логина должна составлять от 8 до 20 символов")
        String login,

        @NotBlank(message = "Пароль не может быть нулевым или незаполненным")
        @Size(min = 8, max = 20, message = "Пароль должен содержать от 8 до 20 символов")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$", message = "Пароль должен содержать по крайней мере одну цифру, одну строчную букву, одну заглавную букву, один специальный символ и не содержать пробелов")
        String password,

        @NotBlank(message = "Адрес электронной почты не может быть пустым")
        @Email(message = "Адрес электронной почты должен быть действительным")
        String email,

        @NotBlank(message = "Номер телефона не может быть нулевым или пустым")
        String phone
) {}

