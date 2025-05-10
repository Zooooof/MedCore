package com.example.MedCore.modules.security.dto;


public record RolePermissionDTO(
        String roleName,
        String permissionName
) {}