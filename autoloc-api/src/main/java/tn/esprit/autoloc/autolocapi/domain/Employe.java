package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmploye;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Le nom est obligatoire.") @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères.")
    private String nom;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Le prénom est obligatoire.") @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères.")
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Le rôle est obligatoire.")
    private RoleEmploye role;

    @ManyToOne
    @JsonBackReference("agence-employes")
    @JoinColumn(name = "agence_id")
    private Agence agence;
}
