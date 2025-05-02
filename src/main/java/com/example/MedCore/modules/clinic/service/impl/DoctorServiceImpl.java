package com.example.MedCore.modules.clinic.service.impl;

import com.example.MedCore.exception.CommonException;
import com.example.MedCore.exception.UserNotFoundException;
import com.example.MedCore.modules.clinic.controller.DoctorController;
import com.example.MedCore.modules.clinic.dto.DoctorCreateDTO;
import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.entity.Clinic;
import com.example.MedCore.modules.clinic.entity.Doctor;
import com.example.MedCore.modules.clinic.entity.Specialization;
import com.example.MedCore.modules.clinic.repository.ClinicRepository;
import com.example.MedCore.modules.clinic.repository.DoctorRepository;
import com.example.MedCore.modules.clinic.repository.SpecializationRepository;
import com.example.MedCore.modules.clinic.service.DoctorService;
import com.example.MedCore.modules.security.entity.Document;
import com.example.MedCore.modules.security.entity.User;
import com.example.MedCore.modules.security.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public Optional<DoctorDTO> getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .map(this::mapToDTO);
    }

    @Transactional
    @Override
    public ResponseEntity<DoctorDTO> createDoctor(DoctorCreateDTO dto) {
        Specialization specialization = specializationRepository.findById(dto.specializationId().getSpecializationId())
                .orElseThrow(() -> new CommonException("Специализация не найдена"));

        Clinic clinic = clinicRepository.findById(dto.clinicId().getClinicId())
                .orElseThrow(() -> new CommonException("Поликлиника не найдена"));

        User user;
        if (dto.userId() != null) {
            user = userRepository.findById(dto.userId().getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден. Пожалуйста, добавьте нового пользователя"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setSpecialization(specialization);
        doctor.setClinic(clinic);
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedAt(LocalDateTime.now());

        doctorRepository.save(doctor);

        return ResponseEntity.ok(mapToDTO(doctor));
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new CommonException("Доктор с ID " + id + " не найден"));

        doctorRepository.delete(doctor);
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
                doctor.getClinic().getName(),
                doctor.getClinic().getAddress(),
                doctor.getSpecialization().getName()
        );
    }
}