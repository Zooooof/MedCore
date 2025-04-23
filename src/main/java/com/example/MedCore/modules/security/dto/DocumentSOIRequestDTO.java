package com.example.MedCore.modules.security.dto;

import lombok.Getter;

public record DocumentSOIRequestDTO(
        String snils,
        String policy,
        String inn
) {}