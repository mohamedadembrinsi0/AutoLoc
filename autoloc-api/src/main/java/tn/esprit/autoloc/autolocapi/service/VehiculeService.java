package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.calcul.TarifStrategy;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.IEquipementRepository;
import tn.esprit.autoloc.autolocapi.repository.IVehiculeRepository;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VehiculeService extends AbstractCrudService<Vehicule> implements IVehiculeService {
    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;
    private final Map<VehiculeOffre, TarifStrategy> strategies;

    public VehiculeService(IVehiculeRepository repository, IAgenceRepository agenceRepository,
                           IEquipementRepository equipementRepository, List<TarifStrategy> strategies) {
        super(repository, Vehicule.class, "Vehicule");
        this.vehiculeRepository = repository;
        this.agenceRepository = agenceRepository;
        this.equipementRepository = equipementRepository;
        this.strategies = strategies.stream().collect(Collectors.toUnmodifiableMap(TarifStrategy::offre, Function.identity()));
    }

    @Override
    public double calculTarif(Long idVehicule, int nombreJours, VehiculeOffre offre) {
        if (nombreJours <= 0 || offre == null) throw new BadRequestException("Durée et offre valides obligatoires");
        Vehicule vehicule = loadEntity(idVehicule);
        TarifStrategy strategy = strategies.get(offre);
        if (strategy == null) throw new BadRequestException("Offre inconnue : " + offre);
        return strategy.calculer(vehicule.getTarifJournalier(), nombreJours)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public Vehicule desaffecterAgence(Long idVehicule) {
        Vehicule vehicule = loadEntity(idVehicule);
        vehicule.setAgence(null);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule affecterAgence(Long idVehicule, Long idAgence) {
        Vehicule vehicule = loadEntity(idVehicule);
        var agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", String.valueOf(idAgence)));
        vehicule.setAgence(agence);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = loadEntity(idVehicule);
        var equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", String.valueOf(idEquipement)));
        vehicule.addEquipement(equipement);
        return vehiculeRepository.save(vehicule);
    }
}
