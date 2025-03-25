package com.example.MedCore.exception;

import com.example.MedCore.modules.security.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponseDTO<T> {
    private int status;
    private String message;
    private T data;
}
