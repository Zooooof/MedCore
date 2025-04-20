package com.example.MedCore.modules.security.dto;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DocumentDTO {
    //TODO использовать record
    private final Long documentId;
    private final String firstname;
    private final String lastname;
    private final String gender;
    private final LocalDate dateOfBirth;

    public DocumentDTO(Long documentId, String firstname, String lastname, String gender, LocalDate dateOfBirth) {
        this.documentId = documentId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}