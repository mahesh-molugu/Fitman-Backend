package com.fitmannation.controller;

import com.fitmannation.dto.PaymentRequest;
import com.fitmannation.dto.PaymentResponse;
import com.fitmannation.model.MembershipPlan;
import com.fitmannation.model.Payment;
import com.fitmannation.model.User;
import com.fitmannation.repository.UserRepository;
import com.fitmannation.service.MembershipService;
import com.fitmannation.service.RazorpayService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://mahesh-molugu.github.io"})
public class PaymentController {

    private final RazorpayService razorpayService;
    private final MembershipService membershipService;
    private final UserRepository userRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponse> createOrder(
            @Valid @RequestBody PaymentRequest request,
            Authentication authentication) throws RazorpayException {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payment payment = razorpayService.createOrder(user, request.getPlan(), request.getDescription());

        PaymentResponse response = PaymentResponse.builder()
                .orderId(payment.getRazorpayOrderId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .keyId(razorpayKeyId)
                .description(payment.getDescription())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @RequestBody Map<String, String> paymentData,
            Authentication authentication) throws RazorpayException {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String orderId = paymentData.get("razorpay_order_id");
        String paymentId = paymentData.get("razorpay_payment_id");
        String signature = paymentData.get("razorpay_signature");

        Payment payment = razorpayService.verifyPayment(orderId, paymentId, signature);

        // Create membership after successful payment
        MembershipPlan plan = MembershipPlan.valueOf(paymentData.get("plan"));
        membershipService.createMembership(user, plan, payment);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Payment verified and membership activated");
        response.put("paymentId", payment.getId());
        response.put("membershipPlan", plan.getName());

        return ResponseEntity.ok(response);
    }
}


