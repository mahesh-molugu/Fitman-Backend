package com.fitmannation.dto;

import com.fitmannation.model.MembershipPlan;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "Membership plan is required")
    private MembershipPlan plan;
    
    private String description;
}



