package com.example.pivothub.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EvidenceResponse {
    private Long id;
    private String imageUrl;
    private Boolean aiValidated;
    private Double latitude;
    private Double longitude;
    private Boolean locationValid;
    private LocalDateTime submittedAt;
    private Long challengeId;
    private String challengeName;
} 