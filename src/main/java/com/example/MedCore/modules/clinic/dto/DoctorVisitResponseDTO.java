package com.example.MedCore.modules.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public record DoctorVisitResponseDTO(
    LocalDateTime visitDatetime,
    int durationMinutes,
    String status,
    Long documentId,
    String firstName,
    String lastName,
    String surname
){}