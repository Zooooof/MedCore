package com.example.MedCore.modules.clinic.repository;

import com.example.MedCore.modules.clinic.entity.Doctor;
import com.example.MedCore.modules.clinic.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.doctorId = :doctorId ORDER BY ds.weekday")
    List<DoctorSchedule> findWeeklyScheduleByDoctorId(@Param("doctorId") Long doctorId);

    Optional<DoctorSchedule> findByDoctorAndWeekday(Doctor doctor, int weekday);
}
