package tn.esprit.autoloc.autolocapi.calcul;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class LongueDureeStrategy implements TarifStrategy {
    private static final BigDecimal SEUIL = new BigDecimal("2000");
    @Override public VehiculeOffre offre() { return VehiculeOffre.LONGUE_DUREE; }
    @Override public BigDecimal calculer(BigDecimal tarif, int jours) {
        BigDecimal total = tarif.multiply(BigDecimal.valueOf(jours)).multiply(new BigDecimal("0.75"));
        return total.compareTo(SEUIL) > 0 ? total.multiply(new BigDecimal("0.95")) : total;
    }
}
