package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.repository.IContratRepository;

@Service
public class ContratService extends AbstractCrudService<Contrat> implements IContratService {
    public ContratService(IContratRepository repository) { super(repository, Contrat.class, "Contrat"); }
}
