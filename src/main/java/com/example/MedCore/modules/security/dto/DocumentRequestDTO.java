package com.example.MedCore.modules.security.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DocumentRequestDTO {
    private String firstname;
    private String lastname;
    private String surname;
    private Long serialAndNumber;
    private LocalDate dateOfBirth;
    private String issuedBy;
    private LocalDate dateIssued;
    private String departmentCode;
    private String gender;
    private String address;
}