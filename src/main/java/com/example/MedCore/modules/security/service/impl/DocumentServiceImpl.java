package com.example.MedCore.modules.security.service.impl;

import com.example.MedCore.modules.security.dto.DocumentDTO;
import com.example.MedCore.modules.security.dto.DocumentRequestDTO;
import com.example.MedCore.modules.security.dto.DocumentSOIRequestDTO;
import com.example.MedCore.modules.security.entity.Document;
import com.example.MedCore.modules.security.repository.DocumentRepository;
import com.example.MedCore.modules.security.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

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
                .orElseThrow(() -> new RuntimeException("Document not found"));
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
        if (documentRepository.existsBySerialAndNumber(requestDTO.getSerialAndNumber())) {
            throw new RuntimeException("Document with this serial and number already exists");
        }

        Document document = new Document();
        document.setFirstname(requestDTO.getFirstname());
        document.setLastname(requestDTO.getLastname());
        document.setSurname(requestDTO.getSurname());
        document.setSerialAndNumber(requestDTO.getSerialAndNumber());
        document.setDateOfBirth(requestDTO.getDateOfBirth());
        document.setIssuedBy(requestDTO.getIssuedBy());
        document.setDateIssued(requestDTO.getDateIssued());
        document.setDepartmentCode(requestDTO.getDepartmentCode());
        document.setGender(Document.Gender.valueOf(requestDTO.getGender().toUpperCase()));
        document.setAddress(requestDTO.getAddress());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        Document savedDocument = documentRepository.save(document);

        return new DocumentDTO(
                savedDocument.getDocumentId(),
                savedDocument.getFirstname(),
                savedDocument.getLastname(),
                savedDocument.getGender().name(),
                savedDocument.getDateOfBirth()
        );
    }

    @Override
    public DocumentDTO updateDocument(Long id, DocumentRequestDTO requestDTO) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setFirstname(requestDTO.getFirstname());
        document.setLastname(requestDTO.getLastname());
        document.setSurname(requestDTO.getSurname());
        document.setSerialAndNumber(requestDTO.getSerialAndNumber());
        document.setDateOfBirth(requestDTO.getDateOfBirth());
        document.setIssuedBy(requestDTO.getIssuedBy());
        document.setDateIssued(requestDTO.getDateIssued());
        document.setDepartmentCode(requestDTO.getDepartmentCode());
        document.setGender(Document.Gender.valueOf(requestDTO.getGender().toUpperCase()));
        document.setAddress(requestDTO.getAddress());
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
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (documentRepository.existsByPolicy(requestDTO.getPolicy())) {
            throw new RuntimeException("Policy already exists");
        }

        if (documentRepository.existsByInn(requestDTO.getInn())) {
            throw new RuntimeException("Inn already exists");
        }

        document.setSnils(requestDTO.getSnils());
        document.setPolicy(requestDTO.getPolicy());
        document.setInn(requestDTO.getInn());
        documentRepository.save(document);

        return "the data has been updated";
    }

}