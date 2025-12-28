package com.fitmannation.controller;

import com.fitmannation.model.MembershipPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://mahesh-molugu.github.io"})
public class PublicController {

    @GetMapping("/plans")
    public ResponseEntity<List<Map<String, Object>>> getMembershipPlans() {
        List<Map<String, Object>> plans = Arrays.stream(MembershipPlan.values())
                .map(plan -> {
                    Map<String, Object> planMap = new HashMap<>();
                    planMap.put("id", plan.name());
                    planMap.put("name", plan.getName());
                    planMap.put("price", plan.getPrice());
                    return planMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(plans);
    }
}


