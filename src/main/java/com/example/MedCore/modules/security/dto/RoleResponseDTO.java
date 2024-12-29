package com.example.MedCore.modules.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleResponseDTO {
    private Long roleId;
    private String roleName;
}
