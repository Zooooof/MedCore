package com.example.MedCore.modules.clinic.service.impl;

import com.example.MedCore.modules.clinic.entity.Specialization;
import com.example.MedCore.modules.clinic.repository.SpecializationRepository;
import com.example.MedCore.modules.clinic.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {
    private final SpecializationRepository specializationRepository;
    @Override
    public List<Specialization> getAllSpecialization() {
        return specializationRepository.findAll();
    }
}
