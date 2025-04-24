package com.example.MedCore.modules.security.controller;

import com.example.MedCore.modules.security.dto.DocumentDTO;
import com.example.MedCore.modules.security.dto.DocumentRequestDTO;
import com.example.MedCore.modules.security.dto.DocumentSOIRequestDTO;
import com.example.MedCore.modules.security.service.DocumentService;
import com.example.MedCore.modules.security.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Документы", description = "Методы для управления документами")
public class DocumentController {
    private final DocumentService documentService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Operation(summary = "Получить все документы", description = "Доступно только с правом VIEW_DOCUMENTS")
    @ApiResponse(responseCode = "200", description = "Успешно получен список документов")
    @PreAuthorize("hasAuthority('VIEW_DOCUMENTS')")
    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        logger.info("Получен запрос на получение всех документов");
        try {
            List<DocumentDTO> documents = documentService.getAllDocuments();
            logger.info("Документы получены успешно: {}", documents.size());
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            logger.error("Ошибка при извлечении документов", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Получить документ по ID", description = "Доступно только с правом VIEW_DOCUMENTS")
    @ApiResponse(responseCode = "200", description = "Документ успешно найден")
    @PreAuthorize("hasAuthority('VIEW_DOCUMENTS')")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @Operation(summary = "Создать документ", description = "Создание нового документа")
    @ApiResponse(responseCode = "200", description = "Документ успешно создан")
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody DocumentRequestDTO requestDTO) {
        return ResponseEntity.ok(documentService.createDocument(requestDTO));
    }

    @Operation(summary = "Обновить документ", description = "Обновление документа по ID (требуются права CRUD_DOCUMENTS)")
    @ApiResponse(responseCode = "200", description = "Документ успешно обновлён")
    @PreAuthorize("hasAuthority('CRUD_DOCUMENTS')")
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(documentService.updateDocument(id, requestDTO));
    }

    @Operation(summary = "Обновить SOI документ", description = "Обновление поля SOI (требуются права CRUD_DOCUMENTS)")
    @ApiResponse(responseCode = "200", description = "Поле SOI успешно обновлено")
    @PreAuthorize("hasAuthority('CRUD_DOCUMENTS')")
    @PutMapping("/put_SOI/{id}")
    public ResponseEntity<String> updateDocumentSOI(
            @PathVariable Long id,
            @RequestBody DocumentSOIRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(documentService.updateDocumentSOI(id, requestDTO));
    }
}
