package com.example.pivothub.repository;

import com.example.pivothub.model.Challenge;
import com.example.pivothub.model.ChallengeMember;
import com.example.pivothub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeMemberRepository extends JpaRepository<ChallengeMember, Long> {
    boolean existsByUserAndChallenge(User user, Challenge challenge);
    Optional<ChallengeMember> findByUserAndChallenge(User user, Challenge challenge);
    
    @Query("SELECT cm FROM ChallengeMember cm JOIN FETCH cm.user WHERE cm.challenge = :challenge")
    List<ChallengeMember> findByChallenge(Challenge challenge);
} 