package com.example.MedCore.modules.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public record RoleResponseDTO(
        Long roleId,
        String roleName
) {}