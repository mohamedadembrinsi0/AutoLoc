package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratDto(Long idContrat,
                         @NotNull(message = "La date de signature est obligatoire.") LocalDate dateSignature,
                         @NotNull(message = "Le montant total est obligatoire.") @DecimalMin(value = "0.0", inclusive = false, message = "Le montant total doit être supérieur à zéro.") BigDecimal montantTotal,
                         boolean valide,
                         @NotNull(message = "La réservation est obligatoire.") @Positive(message = "L'identifiant de la réservation doit être positif.") Long reservationId) { }
