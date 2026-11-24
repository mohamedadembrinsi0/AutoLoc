package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.esprit.autoloc.autolocapi.domain.RoleEmploye;

public record EmployeDto(Long idEmploye,
                         @NotBlank(message = "Le nom est obligatoire.") @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères.") String nom,
                         @NotBlank(message = "Le prénom est obligatoire.") @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères.") String prenom,
                         @NotNull(message = "Le rôle est obligatoire.") RoleEmploye role,
                         Long agenceId) { }
