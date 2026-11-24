package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;

import java.util.List;

public interface IAgenceService extends IGenericService<Agence, Long> {
    boolean hasVehicules(Long id);
    Agence addEmployes(Long id, Agence agence, List<Employe> employes);
    String simuler(Long id, int nombreJours, VehiculeOffre offre);
    Agence simuler(Long id, Agence agence, List<Employe> employes);
}
