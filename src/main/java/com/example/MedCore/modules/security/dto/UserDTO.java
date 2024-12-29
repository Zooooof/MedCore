package com.example.MedCore.modules.security.dto;

import lombok.Getter;

@Getter
public class UserDTO {
    String login;
    String password;
    String email;
    String phone;
    String status;

    public UserDTO(String login, String password, String email, String phone, String name) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.status = name;
    }
}
