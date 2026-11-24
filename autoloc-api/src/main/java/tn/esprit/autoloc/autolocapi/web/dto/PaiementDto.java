package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tn.esprit.autoloc.autolocapi.domain.ModePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaiementDto(Long idPaiement,
                          @NotNull(message = "Le montant est obligatoire.") @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à zéro.") BigDecimal montant,
                          @NotNull(message = "La date de paiement est obligatoire.") LocalDate datePaiement,
                          @NotNull(message = "Le mode de paiement est obligatoire.") ModePaiement modePaiement,
                          @NotNull(message = "Le contrat est obligatoire.") @Positive(message = "L'identifiant du contrat doit être positif.") Long contratId) { }
