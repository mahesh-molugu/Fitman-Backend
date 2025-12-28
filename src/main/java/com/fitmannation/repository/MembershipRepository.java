package com.fitmannation.repository;

import com.fitmannation.model.Membership;
import com.fitmannation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByUser(User user);
    List<Membership> findByUserAndIsActive(User user, Boolean isActive);
    Optional<Membership> findByUserAndIsActiveTrue(User user);
}


