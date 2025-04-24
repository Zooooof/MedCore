package com.example.MedCore.modules.clinic.repository;

import com.example.MedCore.modules.clinic.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {

}
