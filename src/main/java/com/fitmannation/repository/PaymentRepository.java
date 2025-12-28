package com.fitmannation.repository;

import com.fitmannation.model.Payment;
import com.fitmannation.model.PaymentStatus;
import com.fitmannation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String orderId);
    Optional<Payment> findByRazorpayPaymentId(String paymentId);
    List<Payment> findByUser(User user);
    List<Payment> findByStatus(PaymentStatus status);
}



