package com.example.MedCore.modules.security.service;

import com.example.MedCore.modules.security.dto.RoleDTO;
import com.example.MedCore.modules.security.dto.RolePermissionAssignmentDTO;
import com.example.MedCore.modules.security.entity.RoleDB;

import java.util.List;

public interface RoleService {
    List<RoleDB> getAllRoles();
    RoleDB getRoleById(Long roleId);
    RoleDB getRoleByName(String roleName);
    List<RoleDB> getSubordinateRoles(RoleDB parentRole);
    RoleDB addRole(RoleDTO roleDTO);
    void assignPermissionToRole(RolePermissionAssignmentDTO assignmentDTO);
    String getRoleByLogin(String login);
}
