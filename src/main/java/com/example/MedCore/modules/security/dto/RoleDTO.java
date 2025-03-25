package com.example.MedCore.modules.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoleDTO {
    private String roleName;
    private Long parentRoleId;
    private String description;
}