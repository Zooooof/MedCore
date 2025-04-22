package com.example.MedCore.modules.clinic.service.impl;

import com.example.MedCore.modules.clinic.controller.DoctorController;
import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.entity.Doctor;
import com.example.MedCore.modules.clinic.repository.DoctorRepository;
import com.example.MedCore.modules.clinic.service.DoctorService;
import com.example.MedCore.modules.security.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    private DoctorDTO mapToDTO(Doctor doctor) {
        User user = doctor.getUser();

        return new DoctorDTO(
                doctor.getDoctorId(),
                user.getDocument().getFirstname(),
                user.getDocument().getSurname(),
                user.getDocument().getLastname(),
                user.getDocument().getDateOfBirth(),
                user.getDocument().getSerialAndNumber(),
                user.getDocument().getDateIssued(),
                user.getDocument().getDepartmentCode(),
                user.getDocument().getSnils(),
                user.getDocument().getInn(),
                user.getDocument().getPolicy(),
                user.getLogin(),
                user.getEmail(),
                user.getPhone(),
                doctor.getCreatedAt(),
                doctor.getLicenceNumber(),
                doctor.getClinic().getName(),
                doctor.getClinic().getAddress(),
                doctor.getSpecialization().getName()
        );
    }
}