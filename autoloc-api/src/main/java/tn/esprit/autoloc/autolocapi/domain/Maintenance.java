package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "maintenance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaintenance;

    @Column(nullable = false)
    @NotNull(message = "La date de début est obligatoire.")
    private LocalDate dateDebut;

    @Column
    private LocalDate dateFin;

    @Column(length = 500)
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères.")
    private String description;

    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @AssertTrue(message = "La date de fin doit être égale ou postérieure à la date de début.")
    public boolean isPeriodeValide() { return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut); }
}
