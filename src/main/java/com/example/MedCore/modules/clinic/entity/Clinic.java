package com.example.MedCore.modules.clinic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clinics")
public class Clinic {
    @Getter
    @Id
    @GeneratedValue
    @Column(name = "clinic_id")
    Long clinicId;

    @Column(length = 100)
    String name;
    String address;

    @Column(length = 20, nullable = false)
    String phone;

    @Column(length = 100, nullable = false)
    String email;

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
}
