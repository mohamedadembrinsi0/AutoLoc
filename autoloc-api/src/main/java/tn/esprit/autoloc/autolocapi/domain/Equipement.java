package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.LinkedHashSet;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "equipement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le libellé est obligatoire.") @Size(max = 100, message = "Le libellé ne doit pas dépasser 100 caractères.")
    private String libelle;

    @ManyToMany(mappedBy = "equipements")
    @JsonIgnore
    private Set<Vehicule> vehicules = new LinkedHashSet<>();
}
