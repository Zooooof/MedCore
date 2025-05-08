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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ValidatorUser validatorUser;

    @Override
    public UserDTO registerUser(UserRequestDTO requestDTO) {
        validatorUser.validateRegisterRequest(requestDTO);

        if (userRepository.existsByLogin(requestDTO.login())) {
            throw new CommonException("Логин уже существует");
        }

        Document document = documentRepository.findById(requestDTO.document_id())
                .orElseThrow(() -> new CommonException("Документа с таким идентификатором не существует"));

        User user = new User();
        user.setLogin(requestDTO.login());
        user.setPassword_hash(passwordEncoder.encode(requestDTO.password()));
        user.setEmail(requestDTO.email());
        user.setPhone(requestDTO.phone());
        user.setStatus(User.Status.valueOf(requestDTO.status().toUpperCase()));
        user.setDocument(document);

        User savedUser = userRepository.save(user);

        RoleDB patientRole = roleRepository.findById(202L)
                .orElseThrow(() -> new CommonException("Роль пациента не найдена"));
        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(patientRole);
        userRoleRepository.save(userRole);

        logger.info("Пользователь успешно зарегистрировался с ролью пациента: {}", savedUser.getLogin());

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
        logger.info("Получен запрос на аутентификацию для: {}", requestDTO.loginOrEmail());
        validatorUser.validateLoginRequest(requestDTO);

        String loginOrEmail = requestDTO.loginOrEmail();
        String password = requestDTO.password();

        User user;
        if (loginOrEmail.contains("@")) {
            user = userRepository.findByEmail(loginOrEmail)
                    .orElseThrow(() -> new CommonException("Пользователь не найден"));
        } else {
            user = userRepository.findByLogin(loginOrEmail)
                    .orElseThrow(() -> new CommonException("Пользователь не найден"));
        }
        logger.info(user.getUserRoles().toString());

        if (!passwordEncoder.matches(password, user.getPassword_hash())) {
            logger.error("Неверный пароль для пользователя: {}", loginOrEmail);
            throw new CommonException("Неверный пароль");
        }

        List<UserRole> userRoles = userRoleRepository.findWithRolesByUser(user);

        List<String> roles = userRoles.stream()
                .map(ur -> ur.getRole().getRoleName())
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(user.getLogin(), roles);
        logger.info("Сгенерирован JWT-токен для пользователя: {}", user.getLogin());
        return token;
    }

    @Override
    public List<RoleResponseDTO> getUserRoles(Long userId) {
        try {
            List<RoleDB> roles = userRoleRepository.findRolesByUserId(userId);

            return roles.stream()
                    .map(role -> new RoleResponseDTO(role.getRoleId(), role.getRoleName()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Ошибка при выборе ролей для пользователя с идентификатором: {}", userId, e);
            throw e;
        }
    }

    @Override
    public List<PermissionResponseDTO> getUserPermissions(Long userId) {
        try {
            List<Permission> permissions = userRoleRepository.findPermissionsByUserId(userId);

            List<PermissionResponseDTO> permissionDTOs = permissions.stream()
                    .map(permission -> new PermissionResponseDTO(
                            permission.getPermissionsId(),
                            permission.getPermissionName(),
                            permission.getDescription()
                    ))
                    .collect(Collectors.toList());

            return permissionDTOs;
        } catch (Exception e) {
            logger.error("Ошибка при получении разрешений для пользователя с идентификатором: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void assignRoleToUser(UserRoleAssignmentDTO assignmentDTO) {
        User user = userRepository.findById(assignmentDTO.userId())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        RoleDB role = roleRepository.findById(assignmentDTO.roleId())
                .orElseThrow(() -> new CommonException("Роль не найдена"));

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
