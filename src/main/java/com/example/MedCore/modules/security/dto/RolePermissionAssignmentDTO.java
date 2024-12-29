package com.example.MedCore.modules.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermissionAssignmentDTO {
    private Long roleId;
    private Long permissionId;
}