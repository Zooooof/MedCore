package com.example.MedCore.modules.clinic.controller;

import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Получить список врачей", description = "Доступно только с правом VIEW_ROLES")
    @ApiResponse(responseCode = "200", description = "Успешно получен список врачей")
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
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

    @Operation(summary = "Получить врача по айди", description = "Доступно только с правом CRUD_DOCUMENTS")
    @ApiResponse(responseCode = "200", description = "Успешно получен список врачей")
    @PreAuthorize("hasAuthority('CRUD_DOCUMENTS')")
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

    @Operation(summary = "Получить врача по айди", description = "Доступно только с правом CRUD_DOCUMENTS")
    @ApiResponse(responseCode = "200", description = "Успешно получен список врачей")
    @PreAuthorize("hasAuthority('CRUD_DOCUMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity createDoctor(){

    }
}