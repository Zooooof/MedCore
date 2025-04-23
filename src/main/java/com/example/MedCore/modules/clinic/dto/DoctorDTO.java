package com.example.MedCore.modules.clinic.dto;


import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DoctorDTO (
        Long doctorId,
        String firstname,
        String surname,
        String lastname,
        LocalDate dateOfBirth,
        Long serialAndNumber,
        LocalDate dateIssued,
        String departmentCode,
        String snils,
        String inn,
        String policy,
        String login,
        String email,
        String phone,
        LocalDateTime startedWorking,
        String licenceNumber,
        String clinicName,
        String clinicAddress,
        String specialization
) {}
