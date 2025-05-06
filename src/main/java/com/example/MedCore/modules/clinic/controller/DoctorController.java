package com.example.MedCore.modules.clinic.controller;

import com.example.MedCore.exception.CommonException;
import com.example.MedCore.exception.ErrorResponse;
import com.example.MedCore.exception.UserNotFoundException;
import com.example.MedCore.modules.clinic.dto.DoctorCreateDTO;
import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/doctors")
@Tag(name = "Сотрудники", description = "Методы для управления сотрудниками больницы")
public class DoctorController {
    private final DoctorService doctorService;
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = "Получить список врачей", description = "Доступно только с правом CRUD_DOCTOR")
    @ApiResponse(responseCode = "200", description = "Успешно получен список врачей")
    @PreAuthorize("hasAuthority('CRUD_DOCTOR')")
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        logger.info("Получен запрос на получение всех врачей");
        try {
            List<DoctorDTO> doctors = doctorService.getAllDoctors();
            if (doctors.isEmpty()) {
                logger.info("Список врачей пуст");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            logger.error("Произошла ошибка при получении врачей", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @Operation(summary = "Получить врача по айди", description = "Доступно только с правом CRUD_DOCTOR")
    @ApiResponse(responseCode = "200", description = "Успешно получен список врачей")
    @PreAuthorize("hasAuthority('CRUD_DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        logger.info("Получен запрос на получение врача с ID: {}", id);
        try {
            Optional<DoctorDTO> doctor = doctorService.getDoctorById(id);
            if (doctor.isEmpty()) {
                logger.info("Такого врача не существует");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(doctor.get());
        } catch (Exception e) {
            logger.error("Произошла ошибка при получении врача", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Добавить врача", description = "Доступно только с правом CRUD_DOCTOR")
    @ApiResponse(responseCode = "201", description = "Врач успешно зарегистрирован")
    @PreAuthorize("hasAuthority('CRUD_DOCTOR')")
    @PostMapping("/create")
    public ResponseEntity<?> createDoctor(@RequestBody @Valid DoctorCreateDTO doctorCreateDTO) {
        try {
            logger.info("Начало создания врача в контроллере");
            ResponseEntity<DoctorDTO> doctorDTO = doctorService.createDoctor(doctorCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(doctorDTO);
        } catch (UserNotFoundException | CommonException e) {
            logger.error("Произошла ошибка при получении врача", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Ошибка при создании врача: " + e.getMessage());
            logger.error("Произошла ошибка при получении врача", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "Удалить доктора", description = "Удалить доктора (требуются права CRUD_DOCTOR)")
    @ApiResponse(responseCode = "200", description = "Поле успешно удалено")
    @PreAuthorize("hasAuthority('CRUD_DOCTOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoсtor(@PathVariable Long id){
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok("Доктор с ID " + id + " успешно удалён");
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}