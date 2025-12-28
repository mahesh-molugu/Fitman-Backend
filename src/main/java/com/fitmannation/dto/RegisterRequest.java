package com.fitmannation.dto;

import com.fitmannation.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private String phone;
    private UserRole role = UserRole.USER;
    private String fitnessGoal;
    private String experienceLevel;
    private String workoutLocation;
    private String availableTime;
    private String medicalConditions;
}



