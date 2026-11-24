package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;

public record MaintenanceDto(Long idMaintenance,
                             @NotNull(message = "La date de début est obligatoire.") LocalDate dateDebut,
                             LocalDate dateFin,
                             @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères.") String description,
                             @NotNull(message = "Le véhicule est obligatoire.") @Positive(message = "L'identifiant du véhicule doit être positif.") Long vehiculeId) {
    @AssertTrue(message = "La date de fin doit être égale ou postérieure à la date de début.")
    public boolean isPeriodeValide() {
        return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut);
    }
}
