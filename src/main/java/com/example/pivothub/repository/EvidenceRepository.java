package com.example.pivothub.repository;

import com.example.pivothub.model.ChallengeMember;
import com.example.pivothub.model.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, Long> {
    boolean existsByChallengeMemberAndSubmittedAtBetween(
        ChallengeMember challengeMember, 
        LocalDateTime start, 
        LocalDateTime end
    );
    List<Evidence> findByChallengeMemberId(Long id);
} 