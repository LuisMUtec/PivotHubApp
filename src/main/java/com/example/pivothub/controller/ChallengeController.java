package com.example.pivothub.controller;

import com.example.pivothub.dto.ChallengeResponse;
import com.example.pivothub.dto.CreateChallengeRequest;
import com.example.pivothub.dto.ChallengeParticipantResponse;
import com.example.pivothub.model.Challenge;
import com.example.pivothub.model.ChallengeMember;
import com.example.pivothub.model.User;
import com.example.pivothub.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    @Transactional
    public ResponseEntity<ChallengeResponse> createChallenge(
            @RequestBody @Valid CreateChallengeRequest request,
            @AuthenticationPrincipal User user) {
        Challenge challenge = challengeService.createChallenge(request, user);
        return ResponseEntity.ok(ChallengeResponse.fromChallenge(challenge));
    }

    @PostMapping("/{id}/join")
    @Transactional
    public ResponseEntity<Map<String, String>> joinChallenge(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        ChallengeMember member = challengeService.joinChallenge(id, user);
        return ResponseEntity.ok(Map.of(
            "message", 
            String.format("Te uniste al reto \"%s\" exitosamente", member.getChallenge().getName())
        ));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ChallengeResponse>> getAllChallenges() {
        List<Challenge> challenges = challengeService.getAllChallenges();
        List<ChallengeResponse> response = challenges.stream()
                .map(ChallengeResponse::fromChallenge)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/participants")
    @Transactional(readOnly = true)
    public ResponseEntity<List<ChallengeParticipantResponse>> getParticipants(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        List<ChallengeMember> participants = challengeService.getParticipants(id, user);
        
        List<ChallengeParticipantResponse> response = participants.stream()
            .map(member -> ChallengeParticipantResponse.builder()
                .id(member.getUser().getId())
                .name(member.getUser().getName())
                .email(member.getUser().getEmail())
                .joinedAt(member.getJoinedAt())
                .build())
            .toList();
            
        return ResponseEntity.ok(response);
    }
} 