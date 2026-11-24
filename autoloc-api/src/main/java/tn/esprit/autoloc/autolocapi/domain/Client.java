package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Le nom est obligatoire.") @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères.")
    private String nom;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Le prénom est obligatoire.") @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères.")
    private String prenom;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "L'adresse e-mail est obligatoire.") @Email(message = "L'adresse e-mail n'est pas valide.") @Size(max = 100, message = "L'adresse e-mail ne doit pas dépasser 100 caractères.")
    private String email;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Le téléphone est obligatoire.") @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères.")
    private String telephone;

    @Column(nullable = false, unique = true, length = 30)
    @NotBlank(message = "Le numéro de permis est obligatoire.") @Size(max = 30, message = "Le numéro de permis ne doit pas dépasser 30 caractères.")
    private String numPermis;

    @Column(nullable = false)
    @NotNull(message = "La date d'inscription est obligatoire.") @PastOrPresent(message = "La date d'inscription ne peut pas être dans le futur.")
    private LocalDate dateInscription;
}
