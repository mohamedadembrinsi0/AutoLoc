package tn.esprit.autoloc.autolocapi.io;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;

import java.math.BigDecimal;

public interface RapportGenerator {
    String generer(Agence agence, int nombreJours, VehiculeOffre offre, BigDecimal total);
}
