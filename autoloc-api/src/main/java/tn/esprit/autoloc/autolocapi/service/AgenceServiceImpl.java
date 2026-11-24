package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.Utils;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.IEmployeRepository;
import tn.esprit.autoloc.autolocapi.exception.BadRequestException;
import tn.esprit.autoloc.autolocapi.calcul.VehiculeOffre;
import tn.esprit.autoloc.autolocapi.io.RapportGeneratorFactory;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import java.math.BigDecimal;

import java.util.List;

@Service
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceService {
    private final IAgenceRepository agenceRepository;
    private final IEmployeRepository employeRepository;
    private final IVehiculeService vehiculeService;
    private final RapportGeneratorFactory rapportFactory;

    public AgenceServiceImpl(IAgenceRepository agenceRepository, IEmployeRepository employeRepository,
                             IVehiculeService vehiculeService, RapportGeneratorFactory rapportFactory) {
        super(agenceRepository);
        this.agenceRepository = agenceRepository;
        this.employeRepository = employeRepository;
        this.vehiculeService = vehiculeService;
        this.rapportFactory = rapportFactory;
    }

    @Override
    protected String getEntityName() { return "Agence"; }

    @Override
    protected Long getId(Agence agence) { return agence == null ? null : agence.getIdAgence(); }

    @Override
    protected void setFields(Agence target, Agence source) {
        target.setNom(source.getNom());
        target.setVille(source.getVille());
        target.setAdresse(source.getAdresse());
        target.setTelephone(source.getTelephone());
        if (source.getEmployes() != null && !source.getEmployes().isEmpty()) target.setEmployes(source.getEmployes());
        if (source.getEffectif() != null) target.setEffectif(source.getEffectif());
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return agence != null
                && (isNew ? agence.getIdAgence() == null : agence.getIdAgence() != null)
                && Utils.isNotEmpty(agence.getNom()) && agence.getNom().length() <= 100
                && Utils.isNotEmpty(agence.getVille()) && agence.getVille().length() <= 50
                && Utils.isNotEmpty(agence.getAdresse()) && agence.getAdresse().length() <= 200
                && Utils.isNotEmpty(agence.getTelephone()) && agence.getTelephone().length() <= 20;
    }

    @Override public boolean hasVehicules(Long id) { loadEntity(id); return agenceRepository.hasVehicules(id); }

    @Override
    @Transactional
    public Agence addEmployes(Long id, Agence changes, List<Employe> employes) {
        return addEmployesInternal(id, changes, employes);
    }

    private Agence addEmployesInternal(Long id, Agence changes, List<Employe> employes) {
        Agence agence = updateEntity(id, changes);
        if (employes == null || employes.isEmpty()) throw new BadRequestException("La liste des employés est obligatoire");
        List<Employe> validated = employes.stream().map(employe -> {
            if (employe == null || employe.getIdEmploye() != null
                    || !Utils.isNotEmpty(employe.getNom()) || !Utils.isNotEmpty(employe.getPrenom())
                    || employe.getRole() == null) {
                throw new BadRequestException("Chaque employé doit avoir un nom, un prénom et un rôle valides, sans identifiant");
            }
            return employe;
        }).toList();
        for (Employe employe : validated) {
            employe.setAgence(agence);
            agence.addEmploye(employe);
            employeRepository.save(employe);
        }
        agence.setEffectif(agence.getEmployes().size());
        return agenceRepository.save(agence);
    }

    @Override
    @Transactional
    public Agence simuler(Long id, Agence agence, List<Employe> employes) {
        return addEmployesInternal(id, agence, employes);
    }

    @Override
    @Transactional(readOnly = true)
    public String simuler(Long id, int nombreJours, VehiculeOffre offre) {
        if (nombreJours <= 0 || offre == null) throw new BadRequestException("Durée et offre valides obligatoires");
        Agence agence = loadEntity(id);
        BigDecimal total = agence.getVehicules().stream()
                .map(Vehicule::getIdVehicule)
                .map(vehicleId -> BigDecimal.valueOf(vehiculeService.calculTarif(vehicleId, nombreJours, offre)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return rapportFactory.create().generer(agence, nombreJours, offre, total);
    }
}
