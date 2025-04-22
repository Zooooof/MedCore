package com.example.MedCore.modules.security.service.impl;

import com.example.MedCore.modules.security.controller.UserController;
import com.example.MedCore.modules.security.dto.RoleDTO;
import com.example.MedCore.modules.security.dto.RolePermissionAssignmentDTO;
import com.example.MedCore.exception.CommonException;
import com.example.MedCore.modules.security.entity.Permission;
import com.example.MedCore.modules.security.entity.RoleDB;
import com.example.MedCore.modules.security.entity.RolePermission;
import com.example.MedCore.modules.security.repository.PermissionRepository;
import com.example.MedCore.modules.security.repository.RolePermissionRepository;
import com.example.MedCore.modules.security.repository.RoleRepository;
import com.example.MedCore.modules.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;
    private RolePermissionRepository rolePermissionRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<RoleDB> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public RoleDB getRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    @Override
    public RoleDB getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<RoleDB> getSubordinateRoles(RoleDB parentRole) {
        return roleRepository.findByParentRole(parentRole);
    }

    @Override
    public RoleDB addRole(RoleDTO roleDTO) {
        logger.info("Начало добавления роли: {}", roleDTO);
        RoleDB role = new RoleDB();
        role.setRoleName(roleDTO.getRoleName());
        role.setDescription(roleDTO.getDescription());

        if (roleDTO.getParentRoleId() != null) {
            logger.info("Поиск родительской роли с ID: {}", roleDTO.getParentRoleId());
            RoleDB parentRole = roleRepository.findById(roleDTO.getParentRoleId())
                    .orElseThrow(() -> new CommonException("Parent role not found"));
            role.setParentRole(parentRole);
        }

        RoleDB savedRole = roleRepository.save(role);
        logger.info("Роль успешно сохранена: {}", savedRole);
        return savedRole;
    }

    @Override
    public void assignPermissionToRole(RolePermissionAssignmentDTO assignmentDTO) {
        RoleDB role = roleRepository.findById(assignmentDTO.getRoleId())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found"));
        Permission permission = permissionRepository.findById(assignmentDTO.getPermissionId())
                .orElseThrow(() -> new CommonException("Permission not found"));

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        rolePermissionRepository.save(rolePermission);
    }
}