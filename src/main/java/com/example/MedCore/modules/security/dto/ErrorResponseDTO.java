package com.example.MedCore.modules.security.dto;

public record ErrorResponseDTO(
        int status,
        String message
) {}