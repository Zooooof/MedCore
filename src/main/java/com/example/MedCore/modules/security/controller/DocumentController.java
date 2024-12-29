package com.example.MedCore.modules.security.controller;

import com.example.MedCore.modules.security.dto.DocumentDTO;
import com.example.MedCore.modules.security.dto.DocumentRequestDTO;
import com.example.MedCore.modules.security.dto.DocumentSOIRequestDTO;
import com.example.MedCore.modules.security.service.DocumentService;
import com.example.MedCore.modules.security.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentService documentService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @PreAuthorize("hasAuthority('VIEW_DOCUMENTS')")
    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        logger.info("Received request to fetch all documents");
        try {
            List<DocumentDTO> documents = documentService.getAllDocuments();
            logger.info("Documents fetched successfully: {}", documents.size());
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Error while fetching documents", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('VIEW_DOCUMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody DocumentRequestDTO requestDTO) {
        return ResponseEntity.ok(documentService.createDocument(requestDTO));
    }

    @PreAuthorize("hasAuthority('CRUD_DOCUMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(documentService.updateDocument(id, requestDTO));
    }

    @PreAuthorize("hasAuthority('CRUD_DOCUMENTS')")
    @PutMapping("/put_SOI/{id}")
    public ResponseEntity<String> updateDocumentSOI(
            @PathVariable Long id,
            @RequestBody DocumentSOIRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(documentService.updateDocumentSOI(id, requestDTO));
    }
}