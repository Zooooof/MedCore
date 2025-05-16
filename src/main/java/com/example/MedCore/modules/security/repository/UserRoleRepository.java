package com.example.MedCore.modules.security.repository;

import com.example.MedCore.modules.security.entity.Permission;
import com.example.MedCore.modules.security.entity.RoleDB;
import com.example.MedCore.modules.security.entity.User;
import com.example.MedCore.modules.security.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("SELECT ur.role FROM UserRole ur WHERE ur.user.userId = :userId")
    List<RoleDB> findRolesByUserId(@Param("userId") Long userId);
    @Query("SELECT p FROM Permission p " +
            "JOIN RolePermission rp ON p.permissionsId = rp.permission.permissionsId " +
            "JOIN RoleDB r ON rp.role.roleId = r.roleId " +
            "JOIN UserRole ur ON r.roleId = ur.role.roleId " +
            "WHERE ur.user.userId = :userId")
    List<Permission> findPermissionsByUserId(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.role WHERE ur.user = :user")
    List<UserRole> findWithRolesByUser(@Param("user") User user);
}
