package com.example.MedCore.modules.security.controller;


import com.example.MedCore.exception.SuccessResponseDTO;
import com.example.MedCore.modules.security.dto.*;
import com.example.MedCore.modules.security.service.PermissionService;
import com.example.MedCore.modules.security.service.RoleService;
import com.example.MedCore.modules.security.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    private final RoleService roleService;

    private final PermissionService permissionService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> registerUser(@RequestBody UserRequestDTO requestDTO) {
        UserDTO registeredUser = userService.registerUser(requestDTO);
        SuccessResponseDTO response = new SuccessResponseDTO(
                HttpStatus.CREATED.value(),
                "User registered successfully",
                registeredUser
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDTO> authenticate(@Validated @RequestBody UserLoginRequestDTO requestDTO) {
        String token = userService.authenticate(requestDTO);
        SuccessResponseDTO response = new SuccessResponseDTO(
                HttpStatus.ACCEPTED.value(),
                "User authenticated successfully",
                token
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<RoleResponseDTO>> getUserRoles(@PathVariable Long userId) {
        try {
            List<RoleResponseDTO> roles = userService.getUserRoles(userId);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            logger.error("Ошибка при получении ролей пользователя с ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @GetMapping("/{userId}/permissions")
    public ResponseEntity<List<PermissionResponseDTO>> getUserPermissions(@PathVariable Long userId) {
        List<PermissionResponseDTO> permissions = userService.getUserPermissions(userId);
        return ResponseEntity.ok(permissions);
    }

    @PreAuthorize("hasAuthority('ASSIGN_ROLE')")
    @PostMapping("/assign-role")
    public ResponseEntity<Void> assignRoleToUser(@RequestBody UserRoleAssignmentDTO assignmentDTO) {
        userService.assignRoleToUser(assignmentDTO);
        return ResponseEntity.ok().build();
    }
}