package com.example.MedCore.modules.security.service;


import com.example.MedCore.modules.security.dto.*;

import java.util.List;

public interface UserService {
    UserDTO registerUser(UserRequestDTO requestDTO);
    String authenticate(UserLoginRequestDTO requestDTO);
    List<RoleResponseDTO> getUserRoles(Long userId);
    List<PermissionResponseDTO> getUserPermissions(Long userId);
    void assignRoleToUser(UserRoleAssignmentDTO assignmentDTO);
    List<RolePermissionDTO> getRolesAndPermissionsByLogin(String login);
}
