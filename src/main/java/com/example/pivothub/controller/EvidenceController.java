package com.example.pivothub.controller;

import com.example.pivothub.dto.EvidenceResponse;
import com.example.pivothub.dto.SubmitEvidenceRequest;
import com.example.pivothub.model.Evidence;
import com.example.pivothub.model.User;
import com.example.pivothub.service.EvidenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evidences")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;

    @PostMapping
    public ResponseEntity<EvidenceResponse> submitEvidence(
            @Valid @RequestBody SubmitEvidenceRequest request,
            @AuthenticationPrincipal User user) {
        Evidence evidence = evidenceService.submitEvidence(request, user);
        
        EvidenceResponse response = EvidenceResponse.builder()
                .id(evidence.getId())
                .imageUrl(evidence.getImageUrl())
                .aiValidated(evidence.getAiValidated())
                .latitude(evidence.getLatitude())
                .longitude(evidence.getLongitude())
                .locationValid(evidence.getLocationValid())
                .submittedAt(evidence.getSubmittedAt())
                .challengeId(evidence.getChallengeMember().getChallenge().getId())
                .challengeName(evidence.getChallengeMember().getChallenge().getName())
                .build();
                
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<EvidenceResponse>> getAllEvidences(@AuthenticationPrincipal User user) {
        List<EvidenceResponse> evidences = evidenceService.getAllEvidencesbyUser(user);
        return ResponseEntity.ok(evidences);
    }
} 