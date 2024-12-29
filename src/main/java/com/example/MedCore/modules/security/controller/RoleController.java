package com.example.MedCore.modules.security.controller;

import com.example.MedCore.modules.security.dto.RoleDTO;
import com.example.MedCore.modules.security.dto.RolePermissionAssignmentDTO;
import com.example.MedCore.modules.security.entity.RoleDB;
import com.example.MedCore.modules.security.service.RoleService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasAuthority('CRUD_ROLES')")
    @GetMapping
    public ResponseEntity<List<RoleDB>> getAllRoles() {
        List<RoleDB> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PreAuthorize("hasAuthority('CRUD_ROLES')")
    @PostMapping("/create")
    public ResponseEntity<RoleDB> addRole(@RequestBody RoleDTO roleDTO) {
        logger.info("Запрос на создание роли: {}", roleDTO);
        RoleDB role = roleService.addRole(roleDTO);
        logger.info("Роль создана успешно: {}", role);
        return ResponseEntity.ok(role);
    }

    @PreAuthorize("hasAuthority('CRUD_ROLES')")
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDB> getRoleById(@PathVariable Long roleId) {
        RoleDB role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    @PreAuthorize("hasAuthority('CRUD_ROLES')")
    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleDB> getRoleByName(@PathVariable String roleName) {
        RoleDB role = roleService.getRoleByName(roleName);
        return ResponseEntity.ok(role);
    }

    @PreAuthorize("hasAuthority('CRUD_ROLES')")
    @GetMapping("/subordinate/{parentRoleId}")
    public ResponseEntity<List<RoleDB>> getSubordinateRoles(@PathVariable Long parentRoleId) {
        RoleDB parentRole = roleService.getRoleById(parentRoleId);
        List<RoleDB> subordinateRoles = roleService.getSubordinateRoles(parentRole);
        return ResponseEntity.ok(subordinateRoles);
    }

    @PreAuthorize("hasAuthority('ASSIGN_PERMISSION')")
    @PostMapping("/assign-permission")
    public ResponseEntity<Void> assignPermissionToRole(@RequestBody RolePermissionAssignmentDTO assignmentDTO) {
        roleService.assignPermissionToRole(assignmentDTO);
        return ResponseEntity.ok().build();
    }
}