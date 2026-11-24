package tn.esprit.autoloc.autolocapi.io;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import java.math.BigDecimal;

@Component
public class BasicRapportGenerator implements RapportGenerator {
    @Override public String generer(Agence agence, int jours, VehiculeOffre offre, BigDecimal total) {
        return "Simulation agence " + agence.getIdAgence() + " - " + agence.getNom()
                + " - Téléphone : " + agence.getTelephone() + System.lineSeparator()
                + "Véhicules : " + agence.getVehicules().size() + System.lineSeparator()
                + "Durée : " + jours + " jour(s)" + System.lineSeparator()
                + "Offre : " + offre + System.lineSeparator()
                + "Total : " + total.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }
}
