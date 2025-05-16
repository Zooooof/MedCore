package com.example.MedCore.modules.clinic.service;


import com.example.MedCore.modules.clinic.dto.DoctorScheduleResponseDTO;
import com.example.MedCore.modules.clinic.dto.DoctorVisitResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
    List<DoctorScheduleResponseDTO> getWeeklySchedule(Long doctorId);
    List<DoctorVisitResponseDTO> getVisitsOnDate(Long doctorId, LocalDate date);
}
