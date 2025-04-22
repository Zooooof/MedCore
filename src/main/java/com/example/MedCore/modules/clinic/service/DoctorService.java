package com.example.MedCore.modules.clinic.service;

import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.security.dto.DocumentDTO;

public interface DoctorService {
    DoctorDTO getDoctorById(Long id);
}
