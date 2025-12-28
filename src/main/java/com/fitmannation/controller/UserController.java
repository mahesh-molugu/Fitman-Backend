package com.fitmannation.controller;

import com.fitmannation.model.User;
import com.fitmannation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://mahesh-molugu.github.io"})
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            @RequestBody User updatedUser,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update allowed fields
        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        if (updatedUser.getPhone() != null) user.setPhone(updatedUser.getPhone());
        if (updatedUser.getFitnessGoal() != null) user.setFitnessGoal(updatedUser.getFitnessGoal());
        if (updatedUser.getExperienceLevel() != null) user.setExperienceLevel(updatedUser.getExperienceLevel());
        if (updatedUser.getWorkoutLocation() != null) user.setWorkoutLocation(updatedUser.getWorkoutLocation());
        if (updatedUser.getAvailableTime() != null) user.setAvailableTime(updatedUser.getAvailableTime());
        if (updatedUser.getMedicalConditions() != null) user.setMedicalConditions(updatedUser.getMedicalConditions());

        return ResponseEntity.ok(userRepository.save(user));
    }
}


