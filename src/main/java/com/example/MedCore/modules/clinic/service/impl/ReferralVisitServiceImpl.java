package com.example.MedCore.modules.clinic.service.impl;

import com.example.MedCore.modules.clinic.dto.AvailableSlotResponseDTO;
import com.example.MedCore.modules.clinic.dto.CreateReferralVisitRequestDTO;
import com.example.MedCore.modules.clinic.dto.PatientResponseDTO;
import com.example.MedCore.modules.clinic.entity.Doctor;
import com.example.MedCore.modules.clinic.entity.DoctorSchedule;
import com.example.MedCore.modules.clinic.entity.Referral;
import com.example.MedCore.modules.clinic.entity.ReferralVisit;
import com.example.MedCore.modules.clinic.repository.DoctorRepository;
import com.example.MedCore.modules.clinic.repository.DoctorScheduleRepository;
import com.example.MedCore.modules.clinic.repository.ReferralRepository;
import com.example.MedCore.modules.clinic.repository.ReferralVisitRepository;
import com.example.MedCore.modules.clinic.service.ReferralVisitService;
import com.example.MedCore.modules.security.entity.Document;
import com.example.MedCore.modules.security.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class ReferralVisitServiceImpl implements ReferralVisitService {
    private final ReferralRepository referralRepository;
    private final ReferralVisitRepository referralVisitRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DocumentRepository documentRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReferralVisitServiceImpl.class);

    @Transactional
    public void createVisit(CreateReferralVisitRequestDTO request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new EntityNotFoundException("Врач не найден"));

        Document patient = documentRepository.findByFirstnameAndLastnameAndSurname(request.firstName(), request.lastName(), request.surname())
                .orElseThrow(() -> new EntityNotFoundException("Пациент не найден"));

        LocalDateTime visitStart = request.visitDatetime();
        LocalDateTime visitEnd = visitStart.plusMinutes(request.durationMinutes());

        int weekday = visitStart.getDayOfWeek().getValue();
        logger.info("Ищем расписание врача для дня недели: {}", weekday);
        DoctorSchedule schedule = doctorScheduleRepository
                .findByDoctorAndWeekday(doctor, weekday)
                .orElseThrow(() -> new IllegalArgumentException("Нет расписания на этот день"));

        logger.info("Расписание врача найдено: начало {} - конец {}", schedule.getStartTime(), schedule.getEndTime());

        if (visitStart.toLocalTime().isBefore(schedule.getStartTime()) ||
                visitEnd.toLocalTime().isAfter(schedule.getEndTime())) {
            logger.error("Время приема вне расписания врача. Начало: {}, Конец: {}", visitStart, visitEnd);
            throw new IllegalArgumentException("Время приёма вне расписания врача");
        }

        if (referralVisitRepository.existsByDoctorAndTimeRange(doctor.getDoctorId(), visitStart, visitEnd)) {
            logger.error("Врач занят в это время. Начало: {}, Конец: {}", visitStart, visitEnd);
            throw new IllegalStateException("Врач занят в это время");
        }

        logger.error(String.valueOf(doctor.getDoctorId()));
        Referral referral = new Referral();
        referral.setDoctor(doctor);
        referral.setDocument(patient);
        referral.setReason(request.reason());
        logger.info("Создание направления для пациента: {} {} {}. Причина: {}", patient.getFirstname(), patient.getLastname(), patient.getSurname(), request.reason());
        referralRepository.save(referral);

        ReferralVisit visit = new ReferralVisit();
        visit.setReferral(referral);
        visit.setVisitDatetime(visitStart);
        visit.setDurationMinutes(request.durationMinutes());
        visit.setStatus(ReferralVisit.VisitStatus.ЗАПЛАНИРОВАНО);
        logger.info("Создание записи на приём: {}. Время: {}", patient.getFirstname() + " " + patient.getLastname(), visitStart);
        referralVisitRepository.save(visit);

    }

    public List<AvailableSlotResponseDTO> getAvailableSlots(Long doctorId, LocalDate date) {
        List<Object[]> rawSlots = referralVisitRepository.findAvailableSlots(doctorId, date);


        List<AvailableSlotResponseDTO> slots = rawSlots.stream()
                .map(row -> {
                    Timestamp startTimestamp = (Timestamp) row[0];
                    Timestamp endTimestamp = (Timestamp) row[1];

                    LocalDateTime start = startTimestamp.toLocalDateTime();
                    LocalDateTime end = endTimestamp.toLocalDateTime();

                    return new AvailableSlotResponseDTO(start, end);
                })
                .toList();


        return slots;
    }

    public boolean deleteVisit(Long visitId) {
        Optional<ReferralVisit> referralVisit = referralVisitRepository.findById(visitId);

        if (referralVisit.isPresent()) {
            referralVisitRepository.deleteById(visitId);
            return true;
        }

        return false;
    }

    @Override
    public List<PatientResponseDTO> getPatientsByDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId))
            throw new EntityNotFoundException("Врач не найден");

        List<Document> patients = referralVisitRepository.findDistinctPatientsByDoctorId(doctorId);
        return patients.stream()
                .map(doc -> new PatientResponseDTO(
                        doc.getDocumentId(),
                        doc.getFirstname(),
                        doc.getLastname(),
                        doc.getSurname()
                ))
                .toList();
    }
}
