package com.example.MedCore.modules.clinic.service;

import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.security.dto.DocumentDTO;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();
    Optional<DoctorDTO> getDoctorById(Long doctorId);
}
