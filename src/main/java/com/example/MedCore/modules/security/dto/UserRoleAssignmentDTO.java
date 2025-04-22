package com.example.MedCore.modules.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

public record UserRoleAssignmentDTO(
        Long userId,
        Long roleId
) {}