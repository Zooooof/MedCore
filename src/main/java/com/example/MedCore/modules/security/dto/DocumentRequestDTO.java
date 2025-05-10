package com.example.MedCore.modules.security.dto;

import java.time.LocalDate;

public record DocumentRequestDTO(
        String firstname,
        String lastname,
        String surname,
        Long serialAndNumber,
        LocalDate dateOfBirth,
        String issuedBy,
        LocalDate dateIssued,
        String departmentCode,
        String gender,
        String address
) {}
