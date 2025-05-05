package com.example.MedCore.modules.clinic.repository;

import com.example.MedCore.modules.clinic.entity.Doctor;
import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.security.entity.User;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByUser(User user);
}