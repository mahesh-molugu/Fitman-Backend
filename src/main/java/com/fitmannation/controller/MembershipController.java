package com.fitmannation.controller;

import com.fitmannation.model.Membership;
import com.fitmannation.model.User;
import com.fitmannation.repository.UserRepository;
import com.fitmannation.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/memberships")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://mahesh-molugu.github.io"})
public class MembershipController {

    private final MembershipService membershipService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Membership>> getUserMemberships(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(membershipService.getUserMemberships(user));
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveMembership(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Membership membership = membershipService.getActiveMembership(user);
        
        Map<String, Object> response = new HashMap<>();
        if (membership != null) {
            response.put("hasActiveMembership", true);
            response.put("membership", membership);
        } else {
            response.put("hasActiveMembership", false);
        }

        return ResponseEntity.ok(response);
    }
}



