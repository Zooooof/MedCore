package com.example.MedCore.modules.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public record PermissionResponseDTO(
        Long permissionsId,
        String permissionName,
        String description
) {}
