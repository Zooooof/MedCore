package com.example.MedCore.modules.clinic.service.impl;

import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.entity.Doctor;
import com.example.MedCore.modules.clinic.repository.DoctorRepository;
import com.example.MedCore.modules.clinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    @Override
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Доктор не найден"));
        return new DoctorDTO(
                doctor.
        );
    }
}
