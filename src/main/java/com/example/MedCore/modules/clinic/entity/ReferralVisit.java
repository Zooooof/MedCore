package com.example.MedCore.modules.clinic.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "referral_visits")
public class ReferralVisit {
    @Id
    @GeneratedValue
    @Column(name = "visit_id")
    Long visitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referral_id", nullable = false)
    Referral referral;

    @Column(name = "visit_datetime", nullable = false)
    LocalDateTime visitDatetime;

    @Column(name = "duration_minutes")
    int durationMinutes = 30;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    VisitStatus status = VisitStatus.ЗАПЛАНИРОВАНО;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum VisitStatus {
        ЗАПЛАНИРОВАНО,
        ПРОШЛО,
        ОТМЕНЕНО,
        ПЕРЕНЕСЕНО
    }
}
