package com.example.MedCore.modules.clinic.repository;

import com.example.MedCore.modules.clinic.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
}
