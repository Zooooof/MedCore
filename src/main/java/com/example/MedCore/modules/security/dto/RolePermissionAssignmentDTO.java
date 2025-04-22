package com.example.MedCore.modules.security.dto;

import lombok.Getter;
import lombok.Setter;

public record RolePermissionAssignmentDTO(
        Long roleId,
        Long permissionId
) {}