package com.example.MedCore.modules.security.service.impl;

import com.example.MedCore.exception.CommonException;
import com.example.MedCore.modules.security.dto.DocumentDTO;
import com.example.MedCore.modules.security.dto.DocumentRequestDTO;
import com.example.MedCore.modules.security.dto.DocumentSOIRequestDTO;
import com.example.MedCore.modules.security.entity.Document;
import com.example.MedCore.modules.security.repository.DocumentRepository;
import com.example.MedCore.modules.security.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;

    @Override
    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(doc -> new DocumentDTO(
                        doc.getDocumentId(),
                        doc.getFirstname(),
                        doc.getLastname(),
                        doc.getGender().name(),
                        doc.getDateOfBirth()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDTO getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Документ не найден"));
        return new DocumentDTO(
                document.getDocumentId(),
                document.getFirstname(),
                document.getLastname(),
                document.getGender().name(),
                document.getDateOfBirth()
        );
    }

    @Override
    public DocumentDTO createDocument(DocumentRequestDTO requestDTO) {
        if (documentRepository.existsBySerialAndNumber(requestDTO.serialAndNumber())) {
            throw new RuntimeException("Документ с такой серией и номером уже существует");
        }

        Document document = new Document();
        document.setFirstname(requestDTO.firstname());
        document.setLastname(requestDTO.lastname());
        document.setSurname(requestDTO.surname());
        document.setSerialAndNumber(requestDTO.serialAndNumber());
        document.setDateOfBirth(requestDTO.dateOfBirth());
        document.setIssuedBy(requestDTO.issuedBy());
        document.setDateIssued(requestDTO.dateIssued());
        document.setDepartmentCode(requestDTO.departmentCode());
        document.setGender(Document.Gender.valueOf(requestDTO.gender().toUpperCase()));
        document.setAddress(requestDTO.address());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        try {
            Document savedDocument = documentRepository.save(document);
            return new DocumentDTO(
                    savedDocument.getDocumentId(),
                    savedDocument.getFirstname(),
                    savedDocument.getLastname(),
                    savedDocument.getGender().name(),
                    savedDocument.getDateOfBirth()
            );
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении документа: " + e.getMessage(), e);
        }
    }

    @Override
    public DocumentDTO updateDocument(Long id, DocumentRequestDTO requestDTO) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Документ не найден"));

        document.setFirstname(requestDTO.firstname());
        document.setLastname(requestDTO.lastname());
        document.setSurname(requestDTO.surname());
        document.setSerialAndNumber(requestDTO.serialAndNumber());
        document.setDateOfBirth(requestDTO.dateOfBirth());
        document.setIssuedBy(requestDTO.issuedBy());
        document.setDateIssued(requestDTO.dateIssued());
        document.setDepartmentCode(requestDTO.departmentCode());
        document.setGender(Document.Gender.valueOf(requestDTO.gender().toUpperCase()));
        document.setAddress(requestDTO.address());
        document.setUpdatedAt(LocalDateTime.now());
        Document updatedDocument = documentRepository.save(document);

        return new DocumentDTO(
                updatedDocument.getDocumentId(),
                updatedDocument.getFirstname(),
                updatedDocument.getLastname(),
                updatedDocument.getGender().name(),
                updatedDocument.getDateOfBirth()
        );
    }

    @Override
    public String updateDocumentSOI(Long id, DocumentSOIRequestDTO requestDTO) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Документ не найден"));

        if (documentRepository.existsByPolicy(requestDTO.policy())) {
            throw new RuntimeException("Полис уже существует");
        }

        if (documentRepository.existsByInn(requestDTO.inn())) {
            throw new RuntimeException("ИНН уже существует");
        }

        document.setSnils(requestDTO.snils());
        document.setPolicy(requestDTO.policy());
        document.setInn(requestDTO.inn());
        documentRepository.save(document);

        return "данные были обновлены";
    }

    @Override
    public void deleteDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new CommonException("Документ с ID " + id + " не найден"));

        documentRepository.delete(document);
    }
}