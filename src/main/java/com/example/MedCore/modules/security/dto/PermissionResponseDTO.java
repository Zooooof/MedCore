package com.example.MedCore.modules.security.dto;

public record PermissionResponseDTO(
        Long permissionsId,
        String permissionName,
        String description
) {}
