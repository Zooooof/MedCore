package com.example.MedCore.modules.security.repository;

import com.example.MedCore.modules.security.dto.RolePermissionDTO;
import com.example.MedCore.modules.security.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);
    @EntityGraph(attributePaths = {"userRoles.role"})
    Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = {"userRoles.role"})
    Optional<User> findByLogin(String login);

    @Query("SELECT new com.example.MedCore.modules.security.dto.RolePermissionDTO(r.roleName, p.permissionName) " +
            "FROM User u JOIN u.userRoles ur JOIN ur.role r JOIN r.rolePermissions rp JOIN rp.permission p " +
            "WHERE u.login = :login")
    List<RolePermissionDTO> findRolesAndPermissionsByLogin(@Param("login") String login);
}
