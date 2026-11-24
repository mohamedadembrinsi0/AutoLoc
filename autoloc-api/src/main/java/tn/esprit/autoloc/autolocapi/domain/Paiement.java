package tn.esprit.autoloc.autolocapi.domain;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "paiement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaiement;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Le montant est obligatoire.") @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à zéro.")
    private BigDecimal montant;

    @Column(nullable = false)
    @NotNull(message = "La date de paiement est obligatoire.")
    private LocalDate datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Le mode de paiement est obligatoire.")
    private ModePaiement modePaiement;

    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "contrat_id", nullable = false)
    private Contrat contrat;
}
