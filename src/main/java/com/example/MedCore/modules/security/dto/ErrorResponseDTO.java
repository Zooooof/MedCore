package com.example.MedCore.modules.security.dto;

public class ErrorResponseDTO {
    private int status;
    private String message;

    // Конструктор
    public ErrorResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Геттеры и сеттеры
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}