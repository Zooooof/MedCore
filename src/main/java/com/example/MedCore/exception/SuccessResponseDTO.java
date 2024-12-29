package com.example.MedCore.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponseDTO<T> {
    private int status;
    private String message;
    private T data;
}
