package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.LinkedHashSet;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idAgence;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le nom de l'agence est obligatoire.")
    @Size(max = 100, message = "Le nom de l'agence ne doit pas dépasser 100 caractères.")
    private String nom;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "La ville est obligatoire.")
    @Size(max = 50, message = "La ville ne doit pas dépasser 50 caractères.")
    private String ville;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "L'adresse est obligatoire.")
    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères.")
    private String adresse;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Le téléphone est obligatoire.")
    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères.")
    private String telephone;

    @JsonManagedReference("agence-vehicules")
    @OneToMany(mappedBy = "agence", cascade = CascadeType.PERSIST)
    private Set<Vehicule> vehicules = new LinkedHashSet<>();

    @JsonManagedReference("agence-employes")
    @OneToMany(mappedBy = "agence")
    private Set<Employe> employes = new LinkedHashSet<>();

    @Column(nullable = false)
    private Integer effectif = 0;

    public void addVehicule(Vehicule vehicule) {
        if (vehicules == null) vehicules = new LinkedHashSet<>();
        vehicules.add(vehicule);
        vehicule.setAgence(this);
    }

    public void removeVehicule(Vehicule vehicule) {
        vehicules.remove(vehicule);
        if (vehicule.getAgence() == this) {
            vehicule.setAgence(null);
        }
    }

    public void addEmploye(Employe employe) {
        if (employes == null) employes = new LinkedHashSet<>();
        employes.add(employe);
        employe.setAgence(this);
        effectif = employes.size();
    }

    public void removeEmploye(Employe employe) {
        if (employes == null) employes = new LinkedHashSet<>();
        employes.remove(employe);
        if (employe.getAgence() == this) {
            employe.setAgence(null);
        }
        effectif = employes.size();
    }

    @PrePersist
    @PreUpdate
    private void initializeEffectif() {
        if (effectif == null) effectif = employes == null ? 0 : employes.size();
        if (vehicules == null) vehicules = new LinkedHashSet<>();
        if (employes == null) employes = new LinkedHashSet<>();
    }
}
