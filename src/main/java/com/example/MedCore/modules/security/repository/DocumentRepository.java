package com.example.MedCore.modules.security.repository;

import com.example.MedCore.modules.security.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Document d WHERE d.serialAndNumber = :serialAndNumber")
    boolean existsBySerialAndNumber(@Param("serialAndNumber") Long serialAndNumber);
    boolean existsByPolicy(String policy);
    boolean existsByInn(String inn);
    boolean existsById(Long id);
}