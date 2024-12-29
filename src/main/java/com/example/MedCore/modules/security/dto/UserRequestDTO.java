package com.example.MedCore.modules.security.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class UserRequestDTO {
    @NotBlank(message = "Login cannot be null or empty")
    @Size(min = 8, max = 20, message = "Login must be between 8 and 20 characters")
    private String login;

    @NotBlank(message = "Password cannot be null or empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no spaces")
    private String password;

    @NotBlank(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone cannot be null or empty")
    private String phone;

    @NotBlank(message = "Status cannot be null or empty")
    private String status;

    @NotNull(message = "Document ID cannot be null")
    private Long document_id;
}