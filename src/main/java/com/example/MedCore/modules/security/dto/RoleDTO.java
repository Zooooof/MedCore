package com.example.MedCore.modules.security.dto;

import lombok.Builder;
import lombok.Getter;

public record RoleDTO(
        String roleName,
        Long parentRoleId,
        String description
) {}