package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "contrat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;

    @Column(nullable = false)
    @NotNull(message = "La date de signature est obligatoire.")
    private LocalDate dateSignature;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Le montant total est obligatoire.") @DecimalMin(value = "0.0", inclusive = false, message = "Le montant total doit être supérieur à zéro.")
    private BigDecimal montantTotal;

    @Column(nullable = false)
    private boolean valide;

    @OneToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;
}
