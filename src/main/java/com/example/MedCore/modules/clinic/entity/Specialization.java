package com.example.MedCore.modules.clinic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "specialization")
public class Specialization {
    @Id
    @GeneratedValue
    @Column(name = "specialization_id")
    Long specializationId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    String name;
    String description;
}

