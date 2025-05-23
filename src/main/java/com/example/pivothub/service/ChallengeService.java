package com.example.pivothub.service;

import com.example.pivothub.dto.CreateChallengeRequest;
import com.example.pivothub.exception.ChallengeException;
import com.example.pivothub.model.Challenge;
import com.example.pivothub.model.ChallengeMember;
import com.example.pivothub.model.ChallengeStatus;
import com.example.pivothub.model.User;
import com.example.pivothub.repository.ChallengeMemberRepository;
import com.example.pivothub.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeMemberRepository challengeMemberRepository;

    @Transactional
    public Challenge createChallenge(CreateChallengeRequest request, User user) {
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
                .createdBy(user)
                .build();

        // Guardar y retornar el reto
        return challengeRepository.save(challenge);
    }

    @Transactional
    public ChallengeMember joinChallenge(Long challengeId, User user) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeException("Reto no encontrado"));

        if (challengeMemberRepository.existsByUserAndChallenge(user, challenge)) {
            throw new ChallengeException("Ya estás participando en este reto");
        }

        ChallengeMember member = ChallengeMember.builder()
                .challenge(challenge)
                .user(user)
                .joinedAt(LocalDate.now())
                .build();

        return challengeMemberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ChallengeMember> getParticipants(Long challengeId, User user) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeException("Reto no encontrado"));

        // Cargar los miembros con sus usuarios
        List<ChallengeMember> members = challengeMemberRepository.findByChallenge(challenge);
        // Inicializar la colección de usuarios para evitar LazyInitializationException
        members.forEach(member -> member.getUser().getName());
        
        return members;
    }

    private boolean isValidDuration(Integer durationDays) {
        return durationDays != null && 
               (durationDays == 7 || durationDays == 15 || durationDays == 30);
    }
} 