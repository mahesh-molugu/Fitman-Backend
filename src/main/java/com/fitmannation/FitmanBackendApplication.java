package com.fitmannation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FitmanBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitmanBackendApplication.class, args);
    }
}



