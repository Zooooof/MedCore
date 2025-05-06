package com.example.MedCore.modules.clinic.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Запрос на создание направления и записи на приём")
public record CreateReferralVisitRequestDTO(

        @NotNull
        @Schema(description = "ID врача", example = "7")
        Long doctorId,

        @NotNull
        @Schema(description = "Имя пациента", example = "Иван")
        String firstName,

        @NotNull
        @Schema(description = "Фамилия пациента", example = "Иванов")
        String lastName,

        @Schema(description = "Отчество пациента", example = "Иванович")
        String surname,

        @NotNull
        @Future
        @Schema(description = "Дата и время приёма", example = "2025-05-10T10:30:00")
        LocalDateTime visitDatetime,

        @Min(10)
        @Max(120)
        @Schema(description = "Продолжительность приёма (в минутах)", example = "30")
        int durationMinutes,

        @Schema(description = "Причина направления", example = "Плановый осмотр")
        String reason
) {}