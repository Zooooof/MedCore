package com.example.MedCore.modules.clinic.service.impl;

import com.example.MedCore.modules.clinic.dto.DoctorScheduleResponseDTO;
import com.example.MedCore.modules.clinic.dto.DoctorVisitResponseDTO;
import com.example.MedCore.modules.clinic.repository.DoctorScheduleRepository;
import com.example.MedCore.modules.clinic.repository.ReferralVisitRepository;
import com.example.MedCore.modules.clinic.service.DoctorScheduleService;
import com.example.MedCore.modules.security.entity.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final ReferralVisitRepository referralVisitRepository;

    public List<DoctorScheduleResponseDTO> getWeeklySchedule(Long doctorId) {
        return doctorScheduleRepository.findWeeklyScheduleByDoctorId(doctorId)
                .stream()
                .map(s -> new DoctorScheduleResponseDTO(
                        s.getWeekday(),
                        s.getStartTime(),
                        s.getEndTime(),
                        s.getRoomNumber()
                ))
                .collect(Collectors.toList());
    }

    public List<DoctorVisitResponseDTO> getVisitsOnDate(Long doctorId, LocalDate date) {
        return referralVisitRepository.findVisitsWithPatientInfo(doctorId, date)
                .stream()
                .map(rv -> {
                    Document doc = rv.getReferral().getDocument();
                    return new DoctorVisitResponseDTO(
                            rv.getVisitId(),
                            rv.getVisitDatetime(),
                            rv.getDurationMinutes(),
                            rv.getStatus().name(),
                            doc.getDocumentId(),
                            doc.getFirstname(),
                            doc.getLastname(),
                            doc.getSurname()
                    );
                })
                .collect(Collectors.toList());
    }
}