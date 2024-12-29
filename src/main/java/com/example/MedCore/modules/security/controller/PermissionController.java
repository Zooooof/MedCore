package com.example.MedCore.modules.security.controller;

import com.example.MedCore.modules.security.dto.PermissionDTO;
import com.example.MedCore.modules.security.entity.Permission;
import com.example.MedCore.modules.security.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PreAuthorize("hasAuthority('ADD_PERMISSIONS')")
    @PostMapping("/create")
    public ResponseEntity<Permission> addPermission(@RequestBody PermissionDTO permissionDTO) {
        Permission permission = permissionService.addPermission(permissionDTO);
        return ResponseEntity.ok(permission);
    }

    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @GetMapping("/{permissionId}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long permissionId) {
        Permission permission = permissionService.getPermissionById(permissionId);
        return ResponseEntity.ok(permission);
    }

    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @GetMapping("/name/{permissionName}")
    public ResponseEntity<Permission> getPermissionByName(@PathVariable String permissionName) {
        Permission permission = permissionService.getPermissionByName(permissionName);
        return ResponseEntity.ok(permission);
    }
}

