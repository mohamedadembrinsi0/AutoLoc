package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.esprit.autoloc.autolocapi.domain.CategorieVehicule;
import tn.esprit.autoloc.autolocapi.domain.StatutVehicule;

import java.math.BigDecimal;

/** API representation of a vehicle, with validation at the request boundary. */
public record VehiculeDto(
        Long idVehicule,
        @NotBlank(message = "L'immatriculation est obligatoire.")
        @Size(max = 20, message = "L'immatriculation ne doit pas dépasser 20 caractères.")
        String immatriculation,
        @NotBlank(message = "La marque est obligatoire.")
        @Size(max = 50, message = "La marque ne doit pas dépasser 50 caractères.")
        String marque,
        @NotBlank(message = "Le modèle est obligatoire.")
        @Size(max = 50, message = "Le modèle ne doit pas dépasser 50 caractères.")
        String modele,
        @NotNull(message = "La catégorie est obligatoire.")
        CategorieVehicule categorie,
        @NotNull(message = "Le tarif journalier est obligatoire.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif journalier doit être supérieur à zéro.")
        BigDecimal tarifJournalier,
        @NotNull(message = "Le statut est obligatoire.")
        StatutVehicule statut,
        Long agenceId
) { }
