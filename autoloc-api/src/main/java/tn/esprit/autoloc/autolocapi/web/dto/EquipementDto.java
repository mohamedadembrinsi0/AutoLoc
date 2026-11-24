package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipementDto(Long idEquipement,
                            @NotBlank(message = "Le libellé est obligatoire.") @Size(max = 100, message = "Le libellé ne doit pas dépasser 100 caractères.") String libelle) { }
