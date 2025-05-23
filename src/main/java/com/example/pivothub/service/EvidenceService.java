package com.example.pivothub.service;

import com.example.pivothub.dto.EvidenceResponse;
import com.example.pivothub.dto.SubmitEvidenceRequest;
import com.example.pivothub.model.Challenge;
import com.example.pivothub.model.ChallengeMember;
import com.example.pivothub.model.Evidence;
import com.example.pivothub.model.User;
import com.example.pivothub.repository.ChallengeMemberRepository;
import com.example.pivothub.repository.ChallengeRepository;
import com.example.pivothub.repository.EvidenceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMemberRepository challengeMemberRepository;
    private final EvidenceRepository evidenceRepository;

    // Coordenadas de referencia (ejemplo: Ciudad de México)
    private static final double LAT_REF = 19.4326;
    private static final double LON_REF = -99.1332;
    private static final double RADIO_METROS = 1000.0; // 1 km

    @Transactional
    public Evidence submitEvidence(SubmitEvidenceRequest request, User user) {
        // Buscar el reto
        Challenge challenge = challengeRepository.findById(request.getChallengeId())
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        // Buscar el ChallengeMember
        ChallengeMember challengeMember = challengeMemberRepository.findByUserAndChallenge(user, challenge)
                .orElseThrow(() -> new RuntimeException("No estás registrado en este reto"));


        // Simular validación de IA
        boolean aiValid = new Random().nextBoolean();

        // Validar ubicación
        boolean locationValid = isWithinRadius(
            request.getLatitude(), 
            request.getLongitude(), 
            LAT_REF, 
            LON_REF, 
            RADIO_METROS
        );

        // Crear y guardar la evidencia
        Evidence evidence = Evidence.builder()
                .imageUrl(request.getImageUrl())
                .aiValidated(aiValid)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .locationValid(locationValid)
                .challengeMember(challengeMember)
                .build();

        return evidenceRepository.save(evidence);
    }

    private boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, double radiusMeters) {
        final int R = 6371000; // Radio de la Tierra en metros

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance <= radiusMeters;
    }

    @Transactional
    public List<EvidenceResponse> getAllEvidencesbyUser(User user) {
        return evidenceRepository.findByChallengeMemberId(user.getId())
                .stream()
                .map(evidence -> EvidenceResponse.builder()
                        .id(evidence.getId())
                        .imageUrl(evidence.getImageUrl())
                        .aiValidated(evidence.getAiValidated())
                        .latitude(evidence.getLatitude())
                        .longitude(evidence.getLongitude())
                        .locationValid(evidence.getLocationValid())
                        .submittedAt(evidence.getSubmittedAt())
                        .challengeId(evidence.getChallengeMember().getChallenge().getId())
                        .challengeName(evidence.getChallengeMember().getChallenge().getName())
                        .build())
                .collect(Collectors.toList());
    }
} 