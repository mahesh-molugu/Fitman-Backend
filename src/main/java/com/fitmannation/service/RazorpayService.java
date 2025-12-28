package com.fitmannation.service;

import com.fitmannation.model.MembershipPlan;
import com.fitmannation.model.Payment;
import com.fitmannation.model.PaymentStatus;
import com.fitmannation.model.User;
import com.fitmannation.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createOrder(User user, MembershipPlan plan, String description) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        Double amount = plan.getPrice();
        if (amount == 0 || amount == null) {
            throw new RuntimeException("Custom pricing plans require manual processing");
        }

        // Convert to paise (Razorpay uses smallest currency unit)
        int amountInPaise = (int) (amount * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "receipt_" + UUID.randomUUID().toString());
        orderRequest.put("notes", new JSONObject()
                .put("user_id", user.getId().toString())
                .put("plan", plan.name())
                .put("description", description != null ? description : plan.getName())
        );

        Order order = razorpay.orders.create(orderRequest);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(amount);
        payment.setCurrency("INR");
        payment.setDescription(description != null ? description : plan.getName());
        payment.setStatus(PaymentStatus.PENDING);

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment verifyPayment(String orderId, String paymentId, String signature) throws RazorpayException {
        Payment payment = paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        try {
            com.razorpay.Utils.verifyPaymentSignature(options, razorpayKeySecret);
            
            payment.setRazorpayPaymentId(paymentId);
            payment.setRazorpaySignature(signature);
            payment.setStatus(PaymentStatus.SUCCESS);
            
            return paymentRepository.save(payment);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }
    }
}



