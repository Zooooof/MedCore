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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<RoleDB> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public RoleDB getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new UsernameNotFoundException("Роль с идентификатором не найдена: " + roleId));
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
        logger.info("Добавление роли: {}", roleDTO);

        RoleDB role;

        if (roleDTO.parentRoleId() != null) {
            logger.info("Ищем родительскую роль с идентификатором: {}", roleDTO.parentRoleId());
            RoleDB parentRole = roleRepository.findById(roleDTO.parentRoleId())
                    .orElseThrow(() -> new CommonException("Родительская роль не найдена с идентификатором: " + roleDTO.parentRoleId()));
            role = new RoleDB(roleDTO.roleName(), roleDTO.description(), parentRole);
        } else {
            role = new RoleDB(roleDTO.roleName(), roleDTO.description());
        }

        RoleDB savedRole = roleRepository.save(role);
        logger.info("Роль успешно сохранена: {}", savedRole);
        return savedRole;
    }

    @Override
    public void assignPermissionToRole(RolePermissionAssignmentDTO assignmentDTO) {
        RoleDB role = roleRepository.findById(assignmentDTO.roleId())
                .orElseThrow(() -> new UsernameNotFoundException("Роль с идентификатором не найдена: " + assignmentDTO.roleId()));
        Permission permission = permissionRepository.findById(assignmentDTO.permissionId())
                .orElseThrow(() -> new CommonException("Разрешение не найдено с идентификатором: " + assignmentDTO.permissionId()));

        RolePermission rolePermission = new RolePermission(role, permission);
        rolePermissionRepository.save(rolePermission);
    }
}

