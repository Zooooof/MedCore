package com.example.MedCore.modules.clinic.service;

import com.example.MedCore.modules.clinic.dto.DoctorCreateDTO;
import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.entity.Doctor;
import com.example.MedCore.modules.security.dto.DocumentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();
    Optional<DoctorDTO> getDoctorById(Long doctorId);
    ResponseEntity<DoctorDTO> createDoctor(DoctorCreateDTO doctorCreateDTO);
    void deleteDoctor(Long id);
    Long getDoctorIdByLogin(String login);
}
