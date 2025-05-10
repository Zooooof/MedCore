package com.example.MedCore.modules.clinic.dto;

import java.time.LocalTime;

public record DoctorScheduleResponseDTO (
    int weekday,
    LocalTime startTime,
    LocalTime endTime,
    String roomNumber
){};