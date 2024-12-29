package com.example.MedCore.modules.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionResponseDTO {
    private Long permissionsId;
    private String permissionName;
    private String description;
}
