package com.example.MedCore.modules.clinic.dto;

import com.example.MedCore.modules.clinic.entity.Clinic;
import com.example.MedCore.modules.clinic.entity.Specialization;
import com.example.MedCore.modules.security.entity.User;

public record DoctorCreateDTO (
    Specialization specializationId,
    Clinic clinicId,
    User userId
) {}
