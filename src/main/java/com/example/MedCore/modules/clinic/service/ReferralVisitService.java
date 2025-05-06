package com.example.MedCore.modules.clinic.service;

import com.example.MedCore.modules.clinic.dto.AvailableSlotResponseDTO;
import com.example.MedCore.modules.clinic.dto.CreateReferralVisitRequestDTO;
import com.example.MedCore.modules.clinic.dto.PatientResponseDTO;
import com.example.MedCore.modules.security.entity.Document;

import java.time.LocalDate;
import java.util.List;

public interface ReferralVisitService {
    void createVisit(CreateReferralVisitRequestDTO request);
    List<AvailableSlotResponseDTO> getAvailableSlots(Long doctorId, LocalDate date);
    boolean deleteVisit(Long visitId);
    List<PatientResponseDTO> getPatientsByDoctor(Long doctorId);
}
