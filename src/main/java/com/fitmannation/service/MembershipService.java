package com.fitmannation.service;

import com.fitmannation.model.Membership;
import com.fitmannation.model.MembershipPlan;
import com.fitmannation.model.Payment;
import com.fitmannation.model.PaymentStatus;
import com.fitmannation.model.User;
import com.fitmannation.repository.MembershipRepository;
import com.fitmannation.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Membership createMembership(User user, MembershipPlan plan, Payment payment) {
        // Deactivate existing memberships
        List<Membership> activeMemberships = membershipRepository.findByUserAndIsActive(user, true);
        activeMemberships.forEach(m -> m.setIsActive(false));
        membershipRepository.saveAll(activeMemberships);

        // Create new membership
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setPlan(plan);
        membership.setStartDate(LocalDate.now());
        membership.setEndDate(LocalDate.now().plusMonths(1)); // 1 month subscription
        membership.setIsActive(true);
        membership.setIsAutoRenew(false);

        membership = membershipRepository.save(membership);

        // Link payment to membership
        payment.setMembership(membership);
        paymentRepository.save(payment);

        return membership;
    }

    public List<Membership> getUserMemberships(User user) {
        return membershipRepository.findByUser(user);
    }

    public Membership getActiveMembership(User user) {
        return membershipRepository.findByUserAndIsActiveTrue(user)
                .orElse(null);
    }
}



