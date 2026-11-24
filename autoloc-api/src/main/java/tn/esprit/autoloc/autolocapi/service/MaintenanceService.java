package tn.esprit.autoloc.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.domain.Maintenance;
import tn.esprit.autoloc.autolocapi.repository.IMaintenanceRepository;

@Service
public class MaintenanceService extends AbstractCrudService<Maintenance> implements IMaintenanceService {
    public MaintenanceService(IMaintenanceRepository repository) { super(repository, Maintenance.class, "Maintenance"); }
}
