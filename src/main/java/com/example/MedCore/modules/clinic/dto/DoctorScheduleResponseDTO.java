package com.example.MedCore.modules.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

public record DoctorScheduleResponseDTO (
    int weekday,
    LocalTime startTime,
    LocalTime endTime,
    String roomNumber
){};