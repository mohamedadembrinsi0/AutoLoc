package tn.esprit.autoloc.autolocapi.calcul;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class WeekendStrategy implements TarifStrategy {
    @Override public VehiculeOffre offre() { return VehiculeOffre.WEEKEND; }
    @Override public BigDecimal calculer(BigDecimal tarif, int jours) {
        return tarif.multiply(BigDecimal.valueOf(jours)).multiply(new BigDecimal("0.80"));
    }
}
