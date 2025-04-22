package com.example.MedCore.modules.clinic.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class DoctorDTO {
    private final Long doctorId;
    private final String firstName;
    private final String surname;
    private final String lastName;
    private final Date dateOfBirth;
    //TODO доделать
}
