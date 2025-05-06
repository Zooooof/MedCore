package com.example.MedCore.modules.clinic.controller;

import com.example.MedCore.modules.clinic.dto.AvailableSlotResponseDTO;
import com.example.MedCore.modules.clinic.dto.DoctorScheduleResponseDTO;
import com.example.MedCore.modules.clinic.dto.DoctorVisitResponseDTO;
import com.example.MedCore.modules.clinic.service.DoctorScheduleService;
import com.example.MedCore.modules.clinic.service.ReferralVisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@Tag(name = "Расписание врача", description = "API для получения расписания и приёмов врача")
@RequiredArgsConstructor
public class DoctorScheduleController {
    private final DoctorScheduleService doctorScheduleService;
    private final ReferralVisitService referralVisitService;

    @Operation(summary = "Получить недельное расписание врача", description = "Доступно только с правом CRUD_SCHEDULE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "404", description = "Врач не найден")
    })
    @PreAuthorize("hasAuthority('CRUD_SCHEDULE')")
    @GetMapping("/{doctorId}")
    public ResponseEntity<List<DoctorScheduleResponseDTO>> getWeeklySchedule(
            @Parameter(description = "ID врача") @PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorScheduleService.getWeeklySchedule(doctorId));
    }

    @Operation(summary = "Получить список приёмов врача за дату", description = "Доступно только с правом CRUD_SCHEDULE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Неверный формат даты")
    })
    @PreAuthorize("hasAuthority('CRUD_SCHEDULE')")
    @GetMapping("/{doctorId}/busy-slots")
    public ResponseEntity<List<DoctorVisitResponseDTO>> getVisitsOnDate(
            @Parameter(description = "ID врача") @PathVariable Long doctorId,
            @Parameter(description = "Дата (формат YYYY-MM-DD)", example = "2025-05-07")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(doctorScheduleService.getVisitsOnDate(doctorId, date));
    }

    @PreAuthorize("hasAuthority('CRUD_SCHEDULE')")
    @Operation(summary = "Получить свободные слоты врача на дату", description = "Доступно только с правом CRUD_SCHEDULE")
    @GetMapping("/{doctorId}/available-slots")
    public List<AvailableSlotResponseDTO> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return referralVisitService.getAvailableSlots(doctorId, date);
    }
}
