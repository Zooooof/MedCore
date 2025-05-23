package com.example.MedCore.modules.clinic.dto;

import java.time.LocalDateTime;

public record DoctorVisitResponseDTO(
    Long referralId,
    LocalDateTime visitDatetime,
    int durationMinutes,
    String status,
    Long documentId,
    String firstName,
    String lastName,
    String surname
){}