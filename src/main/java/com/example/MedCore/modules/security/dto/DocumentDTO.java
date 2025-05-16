package com.example.MedCore.modules.security.dto;

import java.time.LocalDate;

public record DocumentDTO(
        Long documentId,
        String firstname,
        String lastname,
        String gender,
        LocalDate dateOfBirth
) {}