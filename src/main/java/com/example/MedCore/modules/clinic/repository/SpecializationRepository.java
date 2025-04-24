package com.example.MedCore.modules.clinic.repository;

import com.example.MedCore.modules.clinic.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
}
