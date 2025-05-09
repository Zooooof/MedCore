package com.example.MedCore.modules.clinic.repository;

import com.example.MedCore.modules.clinic.entity.Doctor;
import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.security.entity.User;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByUser(User user);
    @Query("SELECT d.doctorId FROM Doctor d WHERE d.user.login = :login")
    Optional<Long> findDoctorIdByUserLogin(@Param("login") String login);
}