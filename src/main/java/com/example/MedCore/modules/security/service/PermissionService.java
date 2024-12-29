package com.example.MedCore.modules.security.service;

import com.example.MedCore.modules.security.dto.PermissionDTO;
import com.example.MedCore.modules.security.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getAllPermissions();
    Permission getPermissionById(Long permissionId);
    Permission getPermissionByName(String permissionName);
    Permission addPermission(PermissionDTO permissionDTO);
}
