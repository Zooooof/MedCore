package com.example.MedCore.modules.security.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
public class Document {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    Long documentId;
    @Column(nullable = false, length = 60)
    String firstname;
    @Column(nullable = false, length = 80)
    String lastname;
    @Column(length = 80)
    String surname;
    @Column(nullable = false, unique = true)
    Long serialAndNumber;
    @Column(nullable = false)
    LocalDate dateOfBirth;
    @Column(nullable = false, length = 255)
    String issuedBy;
    @Column(nullable = false)
    LocalDate dateIssued;
    @Column(length = 7)
    String departmentCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Gender gender;

    @Column(nullable = false, length = 255)
    String address;
    @Column(length = 11, unique = true)
    String snils;
    @Column(length = 16, unique = true)
    String policy;
    @Column(length = 12, unique = true)
    String inn;

    @Column(nullable = false)
    LocalDateTime createdAt;
    @Column(nullable = false)
    LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Gender {
        MALE, FEMALE
    }

    @OneToMany(mappedBy = "document", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
}