package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Equipement;
import tn.esprit.autoloc.autolocapi.repository.IEquipementRepository;

@Service
public class EquipementService extends AbstractCrudService<Equipement> implements IEquipementService {
    public EquipementService(IEquipementRepository repository) { super(repository, Equipement.class, "Ã‰quipement"); }
}
