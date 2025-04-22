package com.example.MedCore.modules.clinic.service;

import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.security.dto.DocumentDTO;

import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();
}
