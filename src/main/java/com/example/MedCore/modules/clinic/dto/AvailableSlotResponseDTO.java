package com.example.MedCore.modules.clinic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Свободный слот для приёма")
public record AvailableSlotResponseDTO(
        @Schema(description = "Начало слота", example = "2025-05-10T10:00:00")
        LocalDateTime start,

        @Schema(description = "Конец слота", example = "2025-05-10T10:30:00")
        LocalDateTime end
) {}
