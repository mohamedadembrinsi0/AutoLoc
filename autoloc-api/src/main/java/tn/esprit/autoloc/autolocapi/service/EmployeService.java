package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.repository.IEmployeRepository;

@Service
public class EmployeService extends AbstractCrudService<Employe> implements IEmployeService {
    public EmployeService(IEmployeRepository repository) { super(repository, Employe.class, "EmployÃ©"); }
}
