package com.example.MedCore.modules.security.service.impl;

import com.example.MedCore.modules.security.dto.PermissionDTO;
import com.example.MedCore.modules.security.entity.Permission;
import com.example.MedCore.modules.security.repository.PermissionRepository;
import com.example.MedCore.modules.security.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission getPermissionById(Long permissionId) {
        return permissionRepository.findById(permissionId).orElse(null);
    }

    @Override
    public Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName).orElse(null);
    }

    @Override
    public Permission addPermission(PermissionDTO permissionDTO) {
        Permission permission = new Permission(permissionDTO.permissionName(), permissionDTO.description());
        return permissionRepository.save(permission);
    }
}