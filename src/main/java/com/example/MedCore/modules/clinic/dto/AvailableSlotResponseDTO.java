package com.example.MedCore.modules.clinic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record AvailableSlotResponseDTO(
        LocalDateTime start,
        LocalDateTime end
) {}
