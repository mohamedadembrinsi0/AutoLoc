package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Vehicule;

import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;

public interface IVehiculeService extends ICrudService<Vehicule> {
    double calculTarif(Long idVehicule, int nombreJours, VehiculeOffre offre);
    Vehicule desaffecterAgence(Long idVehicule);
    Vehicule affecterAgence(Long idVehicule, Long idAgence);
    Vehicule affecterEquipement(Long idVehicule, Long idEquipement);
}
