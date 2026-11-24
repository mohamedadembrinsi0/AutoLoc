package tn.esprit.autoloc.autolocapi.calcul;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class StandardStrategy implements TarifStrategy {
    @Override public VehiculeOffre offre() { return VehiculeOffre.STANDARD; }
    @Override public BigDecimal calculer(BigDecimal tarif, int jours) { return tarif.multiply(BigDecimal.valueOf(jours)); }
}
