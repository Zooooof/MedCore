package com.example.MedCore.modules.clinic.controller;

import com.example.MedCore.modules.clinic.dto.DoctorDTO;
import com.example.MedCore.modules.clinic.service.DoctorService;
import com.example.MedCore.modules.security.dto.DocumentDTO;
import com.example.MedCore.modules.security.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/doctors")
@Tag(name = "Сотрудники", description = "Методы для управления сотрудниками больницы")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Получить список врачей", description = "Доступно только с правом VIEW_DOCTORS")
    @ApiResponse(responseCode = "200", description = "Успешно получен список врачей")
    @PreAuthorize("hasAuthority('VIEW_DOCTORS')")
    @GetMapping
    private ResponseEntity<List<DoctorDTO>> getAllDoctors(){

    }



    @GetMapping("/{id}")
    private ResponseEntity<List<DoctorDTO>> getDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
}
