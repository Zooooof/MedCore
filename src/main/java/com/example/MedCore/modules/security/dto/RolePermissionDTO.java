package com.example.MedCore.modules.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record RolePermissionDTO(
        String roleName,
        String permissionName
) {}