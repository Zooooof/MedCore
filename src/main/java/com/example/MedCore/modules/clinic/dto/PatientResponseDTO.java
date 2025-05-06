package com.example.MedCore.modules.clinic.dto;

public record PatientResponseDTO(
        Long documentId,
        String firstname,
        String lastname,
        String surname
) {}