package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClientDto(Long idClient,
                        @NotBlank(message = "Le nom est obligatoire.") @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères.") String nom,
                        @NotBlank(message = "Le prénom est obligatoire.") @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères.") String prenom,
                        @NotBlank(message = "L'adresse e-mail est obligatoire.") @Email(message = "L'adresse e-mail n'est pas valide.") @Size(max = 100, message = "L'adresse e-mail ne doit pas dépasser 100 caractères.") String email,
                        @NotBlank(message = "Le téléphone est obligatoire.") @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères.") String telephone,
                        @NotBlank(message = "Le numéro de permis est obligatoire.") @Size(max = 30, message = "Le numéro de permis ne doit pas dépasser 30 caractères.") String numPermis,
                        @NotNull(message = "La date d'inscription est obligatoire.") @PastOrPresent(message = "La date d'inscription ne peut pas être dans le futur.") LocalDate dateInscription) { }
