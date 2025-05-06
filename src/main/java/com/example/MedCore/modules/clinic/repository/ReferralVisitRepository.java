package com.example.MedCore.modules.clinic.repository;

import com.example.MedCore.modules.clinic.entity.ReferralVisit;
import com.example.MedCore.modules.security.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReferralVisitRepository extends JpaRepository<ReferralVisit, Long> {
    @Query("SELECT rv FROM ReferralVisit rv " +
            "JOIN rv.referral r " +
            "JOIN r.document d " +
            "WHERE r.doctor.doctorId = :doctorId AND DATE(rv.visitDatetime) = :date")
    List<ReferralVisit> findVisitsWithPatientInfo(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);


    @Query(value = """
    SELECT EXISTS (
        SELECT 1
        FROM referral_visits rv
        JOIN referrals r ON rv.referral_id = r.referral_id
        WHERE r.doctor_id = :doctorId
          AND rv.visit_datetime < :endTime
          AND rv.visit_datetime + INTERVAL '1 minute' * rv.duration_minutes > :startTime
    )
""", nativeQuery = true)
    boolean existsByDoctorAndTimeRange(
            @Param("doctorId") Long doctorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query(value = """
    WITH doctor_hours AS (
        SELECT start_time, end_time
        FROM doctor_schedule
        WHERE doctor_id = :doctorId 
          AND weekday = EXTRACT(DOW FROM CAST(:date AS DATE))
    ),
    slots AS (
        SELECT generate_series(
            CAST(:date AS TIMESTAMP) + start_time,
            CAST(:date AS TIMESTAMP) + end_time - INTERVAL '30 minutes',
            INTERVAL '30 minutes'
        ) AS slot_start
        FROM doctor_hours
    ),
    busy AS (
        SELECT rv.visit_datetime AS slot_start
        FROM referral_visits rv
        JOIN referrals r ON rv.referral_id = r.referral_id
        WHERE r.doctor_id = :doctorId AND DATE(rv.visit_datetime) = :date
    )
    SELECT s.slot_start, s.slot_start + INTERVAL '30 minutes'
    FROM slots s
    LEFT JOIN busy b ON s.slot_start = b.slot_start
    WHERE b.slot_start IS NULL
""", nativeQuery = true)
    List<Object[]> findAvailableSlots(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date
    );

    Optional<ReferralVisit> findById(Long visitId);

    void deleteById(Long visitId);

    List<ReferralVisit> findByReferral_Doctor_DoctorId(Long doctorId);

    @Query("""
    SELECT DISTINCT d FROM ReferralVisit rv
    JOIN rv.referral r
    JOIN r.document d
    WHERE r.doctor.doctorId = :doctorId
""")
    List<Document> findDistinctPatientsByDoctorId(@Param("doctorId") Long doctorId);
}
