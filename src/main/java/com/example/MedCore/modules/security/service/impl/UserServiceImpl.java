package com.example.MedCore.modules.security.service.impl;

import com.example.MedCore.modules.security.dto.*;
import com.example.MedCore.modules.security.entity.*;
import com.example.MedCore.exception.CommonException;
import com.example.MedCore.modules.security.repository.DocumentRepository;
import com.example.MedCore.modules.security.repository.RoleRepository;
import com.example.MedCore.modules.security.repository.UserRepository;
import com.example.MedCore.modules.security.repository.UserRoleRepository;
import com.example.MedCore.modules.security.securityUser.JwtUtil;
import com.example.MedCore.modules.security.securityUser.Validators.ValidatorUser;
import com.example.MedCore.modules.security.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ValidatorUser validatorUser;


    @Override
    public UserDTO registerUser(UserRequestDTO requestDTO) {
        validatorUser.validateRegisterRequest(requestDTO);

        if (userRepository.existsByLogin(requestDTO.getLogin())) {
            throw new CommonException("Login already exists");
        }

        Document document = documentRepository.findById(requestDTO.getDocument_id())
                .orElseThrow(() -> new CommonException("There is no document with such an id"));

        User user = new User();
        user.setLogin(requestDTO.getLogin());
        user.setPassword_hash(passwordEncoder.encode(requestDTO.getPassword()));
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());
        user.setStatus(User.Status.valueOf(requestDTO.getStatus().toUpperCase()));
        user.setDocument(document);

        User savedUser = userRepository.save(user);

        RoleDB patientRole = roleRepository.findById(202L)
                .orElseThrow(() -> new CommonException("Patient role not found"));
        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(patientRole);
        userRoleRepository.save(userRole);

        logger.info("User registered successfully with patient role: {}", savedUser.getLogin());

        // Возврат DTO пользователя
        return new UserDTO(
                savedUser.getLogin(),
                savedUser.getPassword_hash(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getStatus().name()
        );
    }

    @Transactional
    @Override
    public String authenticate(UserLoginRequestDTO requestDTO) {
        logger.info("Получен запрос на аутентификацию для: {}", requestDTO.getLoginOrEmail());
        validatorUser.validateLoginRequest(requestDTO);

        String loginOrEmail = requestDTO.getLoginOrEmail();
        String password = requestDTO.getPassword();

        User user;
        if (loginOrEmail.contains("@")) {
            user = userRepository.findByEmail(loginOrEmail)
                    .orElseThrow(() -> new CommonException("User not found"));
        } else {
            user = userRepository.findByLogin(loginOrEmail)
                    .orElseThrow(() -> new CommonException("User not found"));
        }

        if (!passwordEncoder.matches(password, user.getPassword_hash())) {
            logger.error("Неверный пароль для пользователя: {}", loginOrEmail);
            throw new CommonException("Invalid password");
        }


        String token = jwtUtil.generateToken(user.getLogin());
        logger.info("Сгенерирован JWT токен для пользователя: {}", user.getLogin());
        return token;
    }


    @Override
    public List<RoleResponseDTO> getUserRoles(Long userId) {
        try {
            List<RoleDB> roles = userRoleRepository.findRolesByUserId(userId);

            // Преобразование в DTO
            return roles.stream()
                    .map(role -> new RoleResponseDTO(role.getRoleId(), role.getRoleName()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Ошибка при поиске ролей для пользователя с ID: {}", userId, e);
            throw e;
        }
    }

    @Override
    public List<PermissionResponseDTO> getUserPermissions(Long userId) {
        try {
            // Прямой доступ через репозиторий
            List<Permission> permissions = userRoleRepository.findPermissionsByUserId(userId);

            // Преобразование сущностей Permission в DTO
            List<PermissionResponseDTO> permissionDTOs = permissions.stream()
                    .map(permission -> new PermissionResponseDTO(
                            permission.getPermissionsId(),
                            permission.getPermissionName(),
                            permission.getDescription()
                    ))
                    .collect(Collectors.toList());

            return permissionDTOs;
        } catch (Exception e) {
            logger.error("Ошибка при поиске прав для пользователя с ID: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void assignRoleToUser(UserRoleAssignmentDTO assignmentDTO) {
        User user = userRepository.findById(assignmentDTO.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        RoleDB role = roleRepository.findById(assignmentDTO.getRoleId())
                .orElseThrow(() -> new CommonException("Role not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
    }

    @Override
    public List<RolePermissionDTO> getRolesAndPermissionsByLogin(String login) {
        try {
            return userRepository.findRolesAndPermissionsByLogin(login);
        } catch (Exception e) {
            logger.error("Ошибка при выборе ролей и разрешений для входа в систему: {}", login, e);
            throw new CommonException("Не удалось получить роли и разрешения");
        }
    }
}
