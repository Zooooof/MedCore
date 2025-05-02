package com.example.MedCore.modules.clinic.service.impl;

import com.example.MedCore.modules.clinic.entity.Clinic;
import com.example.MedCore.modules.clinic.repository.ClinicRepository;
import com.example.MedCore.modules.clinic.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;
    @Override
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }
}
