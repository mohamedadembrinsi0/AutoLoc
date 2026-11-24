package tn.esprit.autoloc.autolocapi.calcul;

import java.math.BigDecimal;

public interface TarifStrategy {
    VehiculeOffre offre();
    BigDecimal calculer(BigDecimal tarifJournalier, int nombreJours);
}
