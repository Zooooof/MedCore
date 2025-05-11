package com.example.MedCore.modules.clinic.service;

import com.example.MedCore.modules.clinic.dto.DoctorCreateDTO;
import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.security.dto.FioDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();
    Optional<DoctorDTO> getDoctorById(Long doctorId);
    ResponseEntity<DoctorDTO> createDoctor(DoctorCreateDTO doctorCreateDTO);
    void deleteDoctor(Long id);
    Long getDoctorIdByFio(FioDTO fioDTO);
}
