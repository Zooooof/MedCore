package com.example.MedCore.modules.clinic.controller;

import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

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
            List<DoctorDTO> doctors = doctorService.getAllDoctors();  // Получаем список врачей
            if (doctors.isEmpty()) {
                logger.info("Список врачей пуст");
                return ResponseEntity.noContent().build();  // Возвращаем статус 204 No Content, если список пуст
            }
            return ResponseEntity.ok(doctors);  // Возвращаем список врачей с HTTP статусом 200 OK
        } catch (Exception e) {
            logger.error("Произошла ошибка при получении врачей", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)  // Возвращаем статус 500
                    .body(Collections.emptyList());  // Возвращаем пустой список, если произошла ошибка
        }
    }
}