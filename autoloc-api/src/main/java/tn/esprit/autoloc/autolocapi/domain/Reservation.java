package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @Column(nullable = false)
    @NotNull(message = "La date de début est obligatoire.")
    private LocalDate dateDebut;

    @Column(nullable = false)
    @NotNull(message = "La date de fin est obligatoire.")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Le statut est obligatoire.")
    private StatutReservation statut;

    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @AssertTrue(message = "La date de fin doit être égale ou postérieure à la date de début.")
    public boolean isPeriodeValide() { return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut); }
}
