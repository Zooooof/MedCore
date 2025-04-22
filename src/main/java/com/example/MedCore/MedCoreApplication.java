package com.example.MedCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.MedCore")
public class MedCoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(MedCoreApplication.class, args);
	}
}
