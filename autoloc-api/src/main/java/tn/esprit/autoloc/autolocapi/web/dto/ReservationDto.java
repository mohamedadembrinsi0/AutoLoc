package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.AssertTrue;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;

import java.time.LocalDate;

public record ReservationDto(Long idReservation,
                             @NotNull(message = "La date de début est obligatoire.") LocalDate dateDebut,
                             @NotNull(message = "La date de fin est obligatoire.") LocalDate dateFin,
                             @NotNull(message = "Le statut est obligatoire.") StatutReservation statut,
                             @NotNull(message = "Le véhicule est obligatoire.") @Positive(message = "L'identifiant du véhicule doit être positif.") Long vehiculeId,
                             @NotNull(message = "Le client est obligatoire.") @Positive(message = "L'identifiant du client doit être positif.") Long clientId) {
    @AssertTrue(message = "La date de fin doit être égale ou postérieure à la date de début.")
    public boolean isPeriodeValide() {
        return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut);
    }
}
