package com.example.pivothub.service;

import com.example.pivothub.dto.CreateChallengeRequest;
import com.example.pivothub.model.Challenge;
import com.example.pivothub.model.ChallengeMember;
import com.example.pivothub.model.ChallengeStatus;
import com.example.pivothub.model.User;
import com.example.pivothub.repository.ChallengeMemberRepository;
import com.example.pivothub.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMemberRepository challengeMemberRepository;

    @Transactional
    public Challenge createChallenge(CreateChallengeRequest request, User creator) {
        // Validar durationDays
        if (!isValidDuration(request.getDurationDays())) {
            throw new IllegalArgumentException("La duración debe ser 7, 15 o 30 días");
        }

        // Construir el reto
        Challenge challenge = Challenge.builder()
                .name(request.getName())
                .description(request.getDescription())
                .durationDays(request.getDurationDays())
                .penaltyAmount(request.getPenaltyAmount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(ChallengeStatus.CREATED)
                .createdBy(creator)
                .build();

        // Guardar y retornar el reto
        return challengeRepository.save(challenge);
    }

    @Transactional
    public void joinChallenge(Long challengeId, User user) {
        // Buscar el reto
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        // Verificar si el usuario ya está unido
        if (challengeMemberRepository.existsByUserAndChallenge(user, challenge)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya estás unido a este reto");
        }

        // Crear nuevo miembro
        ChallengeMember member = ChallengeMember.builder()
                .challenge(challenge)
                .user(user)
                .joinedAt(LocalDate.now())
                .progressDays(0)
                .totalPenalties(BigDecimal.ZERO)
                .build();

        // Guardar miembro
        challengeMemberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    private boolean isValidDuration(Integer durationDays) {
        return durationDays != null && 
               (durationDays == 7 || durationDays == 15 || durationDays == 30);
    }
} 