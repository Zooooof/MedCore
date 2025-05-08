package com.example.MedCore;

import com.example.MedCore.exception.CommonException;
import com.example.MedCore.modules.clinic.dto.DoctorCreateDTO;
import com.example.MedCore.modules.clinic.entity.*;
import com.example.MedCore.modules.clinic.repository.*;
import com.example.MedCore.modules.clinic.service.impl.DoctorServiceImpl;
import com.example.MedCore.modules.security.entity.*;
import com.example.MedCore.modules.security.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Mock private DoctorRepository doctorRepository;
    @Mock private SpecializationRepository specializationRepository;
    @Mock private ClinicRepository clinicRepository;
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private DocumentRepository documentRepository;
    @Mock private UserRoleRepository userRoleRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDoctor_successfullyCreatesDoctor() {
        DoctorCreateDTO dto = new DoctorCreateDTO(
                1L, 2L, "Фёдор",
                "Добрынин", "Никитич",
                "login123123", "Password123!", "Exampleemail@gmail.com",
                "+79995678901"
        );

        Document doc = new Document();
        when(documentRepository.findByFirstnameAndLastnameAndSurname("Фёдор", "Добрынин", "Никитич"))
                .thenReturn(Optional.of(doc));

        when(userRepository.findByDocument(doc)).thenReturn(Optional.empty());
        when(userRepository.existsByLogin("login123123")).thenReturn(false);

        User user = new User();
        user.setLogin("ivan.petrov");
        user.setDocument(doc);
        when(passwordEncoder.encode("Password123!")).thenReturn("hashedPwd");
        when(userRepository.save(any(User.class))).thenReturn(user);

        RoleDB role = new RoleDB();
        when(roleRepository.findById(202L)).thenReturn(Optional.of(role));

        Specialization spec = new Specialization();
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(spec));

        Clinic clinic = new Clinic();
        when(clinicRepository.findById(2L)).thenReturn(Optional.of(clinic));

        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        ResponseEntity<?> response = doctorService.createDoctor(dto);

        // THEN
        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository).save(any(User.class));
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void createDoctor_whenUserExistsAsDoctor_throwsException() {
        Document doc = new Document();
        User user = new User();
        user.setDocument(doc);

        DoctorCreateDTO dto = new DoctorCreateDTO(
                2L, 2L, "Фёдор",
                "Добрынин", "Никитич",
                "login123123", "Password123!", "Exampleemail@gmail.com",
                "+79995678901"
        );

        when(documentRepository.findByFirstnameAndLastnameAndSurname("Фёдор", "Добрынин", "Никитич"))
                .thenReturn(Optional.of(doc));

        when(userRepository.findByDocument(doc)).thenReturn(Optional.of(user));
        when(doctorRepository.existsByUser(user)).thenReturn(true);

        assertThrows(CommonException.class, () -> doctorService.createDoctor(dto));
    }

    @Test
    void createDoctor_documentNotFound_throwsException() {
        DoctorCreateDTO dto = new DoctorCreateDTO(
                1L, 2L, "Иван", "Петров", "Сергеевич",
                "ivan.petrov", "password123", "ivan@example.com",
                "+79001234567"
        );

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(new Specialization()));
        when(clinicRepository.findById(2L)).thenReturn(Optional.of(new Clinic()));

        when(documentRepository.findByFirstnameAndLastnameAndSurname("Иван", "Петров", "Сергеевич"))
                .thenReturn(Optional.empty());

        CommonException ex = assertThrows(CommonException.class, () -> {
            doctorService.createDoctor(dto);
        });

        assertEquals("Ошибка при создании врача: Документ не найден по введённым данным", ex.getMessage());
    }

    @Test
    void createDoctor_userAlreadyDoctor_throwsException() {
        DoctorCreateDTO dto = new DoctorCreateDTO(
                1L, 2L, "Иван", "Петров", "Сергеевич",
                "ivan.petrov", "password123", "ivan@example.com",
                "+79001234567"
        );

        Document doc = new Document();
        User user = new User();
        user.setDocument(doc);

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(new Specialization()));
        when(clinicRepository.findById(2L)).thenReturn(Optional.of(new Clinic()));
        when(documentRepository.findByFirstnameAndLastnameAndSurname("Иван", "Петров", "Сергеевич"))
                .thenReturn(Optional.of(doc));
        when(userRepository.findByDocument(doc)).thenReturn(Optional.of(user));
        when(doctorRepository.existsByUser(user)).thenReturn(true);

        CommonException ex = assertThrows(CommonException.class, () -> {
            doctorService.createDoctor(dto);
        });

        assertEquals("Ошибка при создании врача: Этот пользователь уже зарегистрирован как врач", ex.getMessage());
    }

    @Test
    void createDoctor_loginAlreadyExists_throwsException() {
        DoctorCreateDTO dto = new DoctorCreateDTO(
                1L, 2L, "Иван", "Петров", "Сергеевич",
                "ivan.petrov", "password123", "ivan@example.com",
                "+79001234567"
        );

        Document doc = new Document();

        when(specializationRepository.findById(1L)).thenReturn(Optional.of(new Specialization()));
        when(clinicRepository.findById(2L)).thenReturn(Optional.of(new Clinic()));
        when(documentRepository.findByFirstnameAndLastnameAndSurname("Иван", "Петров", "Сергеевич"))
                .thenReturn(Optional.of(doc));
        when(userRepository.findByDocument(doc)).thenReturn(Optional.empty());
        when(userRepository.existsByLogin("ivan.petrov")).thenReturn(true);

        CommonException ex = assertThrows(CommonException.class, () -> {
            doctorService.createDoctor(dto);
        });

        assertEquals("Ошибка при создании врача: Login уже существует", ex.getMessage());
    }
}