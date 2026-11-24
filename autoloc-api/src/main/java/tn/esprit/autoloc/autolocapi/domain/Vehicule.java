package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    @Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "L'immatriculation est obligatoire.") @Size(max = 20, message = "L'immatriculation ne doit pas dépasser 20 caractères.")
    private String immatriculation;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "La marque est obligatoire.") @Size(max = 50, message = "La marque ne doit pas dépasser 50 caractères.")
    private String marque;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Le modèle est obligatoire.") @Size(max = 50, message = "Le modèle ne doit pas dépasser 50 caractères.")
    private String modele;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "La catégorie est obligatoire.")
    private CategorieVehicule categorie;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Le tarif journalier est obligatoire.") @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif journalier doit être supérieur à zéro.")
    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Le statut est obligatoire.")
    private StatutVehicule statut;

    @ManyToOne
    @JsonBackReference("agence-vehicules")
    private Agence agence;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "vehicule_equipement",
            joinColumns = @JoinColumn(name = "vehicule_id"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id"))
    private Set<Equipement> equipements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "vehicule")
    @JsonIgnore
    private Set<Reservation> reservations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "vehicule")
    @JsonIgnore
    private Set<Maintenance> maintenances = new LinkedHashSet<>();

    public void addEquipement(Equipement equipement) {
        equipements.add(equipement);
        equipement.getVehicules().add(this);
    }

    public void removeEquipement(Equipement equipement) {
        equipements.remove(equipement);
        equipement.getVehicules().remove(this);
    }
}
