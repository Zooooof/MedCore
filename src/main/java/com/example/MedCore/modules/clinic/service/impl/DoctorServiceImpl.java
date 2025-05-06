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
import com.example.MedCore.modules.security.entity.RoleDB;
import com.example.MedCore.modules.security.entity.User;
import com.example.MedCore.modules.security.entity.UserRole;
import com.example.MedCore.modules.security.repository.DocumentRepository;
import com.example.MedCore.modules.security.repository.RoleRepository;
import com.example.MedCore.modules.security.repository.UserRepository;
import com.example.MedCore.modules.security.repository.UserRoleRepository;
import com.example.MedCore.modules.security.service.impl.DocumentServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final RoleRepository roleRepository;
    private final DocumentRepository documentRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

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
        try {
            // Логируем начало выполнения метода
            logger.info("Начало создания врача: {}", dto);

            Specialization specialization = specializationRepository.findById(dto.specializationId())
                    .orElseThrow(() -> new CommonException("Специализация не найдена"));

            Clinic clinic = clinicRepository.findById(dto.clinicId())
                    .orElseThrow(() -> new CommonException("Поликлиника не найдена"));

            // Поиск документа по ФИО
            Document document = documentRepository.findByFirstnameAndLastnameAndSurname(
                    dto.firstname(),
                    dto.lastname(),
                    dto.surname()
            ).orElseThrow(() -> new CommonException("Документ не найден по введённым данным"));

            // Поиск пользователя по документу
            Optional<User> existingUserOpt = userRepository.findByDocument(document);
            User user;

            if (existingUserOpt.isPresent()) {
                user = existingUserOpt.get();

                // Проверка — не зарегистрирован ли он уже как доктор
                if (doctorRepository.existsByUser(user)) {
                    throw new CommonException("Этот пользователь уже зарегистрирован как врач");
                }
            } else {
                // Новый пользователь
                if (userRepository.existsByLogin(dto.login())) {
                    throw new CommonException("Login уже существует");
                }

                user = new User();
                user.setLogin(dto.login());
                user.setPassword_hash(passwordEncoder.encode(dto.password()));
                user.setEmail(dto.email());
                user.setPhone(dto.phone());
                user.setStatus(User.Status.ACTIVE);
                user.setDocument(document);
                userRepository.save(user);

                RoleDB patientRole = roleRepository.findById(202L)
                        .orElseThrow(() -> new CommonException("Patient role not found"));
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(patientRole);
                userRoleRepository.save(userRole);
            }

            // Создание доктора
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setSpecialization(specialization);
            doctor.setClinic(clinic);
            doctor.setCreatedAt(LocalDateTime.now());
            doctor.setUpdatedAt(LocalDateTime.now());

            doctorRepository.save(doctor);

            return ResponseEntity.ok(mapToDTO(doctor));
        } catch (CommonException e) {
            logger.error("Ошибка при создании врача: {}", e.getMessage());
            throw new CommonException("Ошибка при создании врача: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Необработанная ошибка при создании врача: ", e);
            throw new CommonException("Ошибка при создании врача: " + e.getMessage());
        }
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