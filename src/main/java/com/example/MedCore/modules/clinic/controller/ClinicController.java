package com.example.MedCore.modules.clinic.controller;

import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.entity.Clinic;
import com.example.MedCore.modules.clinic.service.ClinicService;
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
@RequestMapping("/api/v1/clinics")
public class ClinicController {
    private final ClinicService clinicService;
    private static final Logger logger = LoggerFactory.getLogger(ClinicController.class);

    @Operation(summary = "Получить список Поликлиник", description = "Доступно только с правом VIEW_ROLES")
    @ApiResponse(responseCode = "200", description = "Успешно получен список поликлиник")
    @PreAuthorize("hasAuthority('VIEW_ROLES')")
    @GetMapping
    public ResponseEntity<List<Clinic>> getAllDoctors() {
        logger.info("Получен запрос на получение всех поликлиник");
        try {
            List<Clinic> clinics = clinicService.getAllClinics();
            if (clinics.isEmpty()) {
                logger.info("Список поликлиник пуст");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(clinics);
        } catch (Exception e) {
            logger.error("Произошла ошибка при получении поликлиник", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}
