package com.example.MedCore.modules.clinic.controller;

import com.example.MedCore.modules.clinic.entity.Clinic;
import com.example.MedCore.modules.clinic.entity.Specialization;
import com.example.MedCore.modules.clinic.service.ClinicService;
import com.example.MedCore.modules.clinic.service.SpecializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/specializations")
public class SpecializationController {
    private final SpecializationService specializationService;
    private static final Logger logger = LoggerFactory.getLogger(ClinicController.class);

    @Operation(summary = "Получить список специализаций", description = "Доступно только с правом VIEW_ROLES")
    @ApiResponse(responseCode = "200", description = "Успешно получен список специализаций")
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping
    public ResponseEntity<List<Specialization>> getAllDoctors() {
        logger.info("Получен запрос на получение всех специализаций");
        try {
            List<Specialization> specializations = specializationService.getAllSpecialization();
            if (specializations.isEmpty()) {
                logger.info("Список специализаций пуст");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(specializations);
        } catch (Exception e) {
            logger.error("Произошла ошибка при получении специализаций", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}
