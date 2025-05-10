package com.example.MedCore.modules.clinic.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateReferralVisitRequestDTO(

        @NotNull
        Long doctorId,

        @NotNull
        String firstName,

        @NotNull
        String lastName,

        String surname,

        @NotNull
        @Future
        LocalDateTime visitDatetime,

        @Min(10)
        @Max(120)
        int durationMinutes,

        String reason
) {}