package com.example.pivothub.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChallengeRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    private String description;
    
    @NotNull(message = "La duración es obligatoria")
    @Min(value = 7, message = "La duración mínima es 7 días")
    @Max(value = 30, message = "La duración máxima es 30 días")
    private Integer durationDays;
    
    @NotNull(message = "El monto de penalización es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto de penalización debe ser mayor a 0")
    private BigDecimal penaltyAmount;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio debe ser presente o futura")
    private LocalDate startDate;
    
    @NotNull(message = "La fecha de fin es obligatoria")
    @FutureOrPresent(message = "La fecha de fin debe ser presente o futura")
    private LocalDate endDate;
} 