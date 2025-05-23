package com.example.pivothub.dto;

import com.example.pivothub.model.Challenge;
import com.example.pivothub.model.ChallengeStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ChallengeResponse {
    private Long id;
    private String name;
    private String description;
    private Integer durationDays;
    private BigDecimal penaltyAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private ChallengeStatus status;
    private String creatorName;
    private String creatorEmail;

    public static ChallengeResponse fromChallenge(Challenge challenge) {
        return ChallengeResponse.builder()
                .id(challenge.getId())
                .name(challenge.getName())
                .description(challenge.getDescription())
                .durationDays(challenge.getDurationDays())
                .penaltyAmount(challenge.getPenaltyAmount())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .status(challenge.getStatus())
                .creatorName(challenge.getCreatedBy().getName())
                .creatorEmail(challenge.getCreatedBy().getEmail())
                .build();
    }
} 