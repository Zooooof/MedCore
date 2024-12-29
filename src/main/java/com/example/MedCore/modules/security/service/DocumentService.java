package com.example.MedCore.modules.security.service;

import com.example.MedCore.modules.security.dto.DocumentDTO;
import com.example.MedCore.modules.security.dto.DocumentRequestDTO;
import com.example.MedCore.modules.security.dto.DocumentSOIRequestDTO;

import java.util.List;

public interface DocumentService {
    List<DocumentDTO> getAllDocuments();
    DocumentDTO getDocumentById(Long id);
    DocumentDTO createDocument(DocumentRequestDTO requestDTO);
    DocumentDTO updateDocument(Long id, DocumentRequestDTO requestDTO);
    String updateDocumentSOI(Long id, DocumentSOIRequestDTO requestDTO);
}