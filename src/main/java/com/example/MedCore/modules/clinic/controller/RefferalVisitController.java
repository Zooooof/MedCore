package com.example.MedCore.modules.clinic.controller;

import com.example.MedCore.exception.SuccessResponseDTO;
import com.example.MedCore.modules.clinic.dto.CreateReferralVisitRequestDTO;
import com.example.MedCore.modules.clinic.dto.PatientResponseDTO;
import com.example.MedCore.modules.clinic.service.ReferralVisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/visits")
@Tag(name = "Приёмы к врачу", description = "API для приёмов к врачу")
@RequiredArgsConstructor
public class RefferalVisitController {
    private final ReferralVisitService referralVisitService;

    @Operation(
            summary = "Записать пациента на приём к врачу",
            description = "Доступно только с правом CRUD_SCHEDULE",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateReferralVisitRequestDTO.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешная запись",
                    content = @Content(schema = @Schema(implementation = SuccessResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка ввода"),
            @ApiResponse(responseCode = "409", description = "Время занято")
    })
    @PreAuthorize("hasAuthority('CRUD_SCHEDULE')")
    @PostMapping("/create-visits")
    public ResponseEntity bookVisit(@Valid @RequestBody CreateReferralVisitRequestDTO request) {
        referralVisitService.createVisit(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Запись успешно создана");
    }

    @Operation(summary = "Отменить запись пациента к врачу",
            description = "Отменить запись по ID визита. Доступно только с правом CRUD_SCHEDULE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запись отменена успешно"),
            @ApiResponse(responseCode = "404", description = "Запись не найдена")
    })
    @PreAuthorize("hasAuthority('CRUD_SCHEDULE')")
    @DeleteMapping("/{visitId}")
    public ResponseEntity<String> cancelVisit(@Parameter(description = "ID визита") @PathVariable Long visitId) {
        boolean isCancelled = referralVisitService.deleteVisit(visitId);

        if (isCancelled) {
            return ResponseEntity.ok("Запись отменена успешно.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Запись не найдена.");
        }
    }

    @Operation(summary = "Получить список пациентов, записанных к опрдееленному врачу",
            description = "Доступно только с правом CRUD_SCHEDULE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ")
    })
    @PreAuthorize("hasAuthority('CRUD_SCHEDULE')")
    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<?> getPatientsByDoctor(@PathVariable Long doctorId) {
        try {
            List<PatientResponseDTO> patients = referralVisitService.getPatientsByDoctor(doctorId);
            return ResponseEntity.ok(patients);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Врач с ID " + doctorId + " не найден");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка сервера: " + e.getMessage());
        }
    }
}
