package tn.esprit.autoloc.autolocapi.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;
import tn.esprit.autoloc.autolocapi.repository.IEmployeRepository;
import tn.esprit.autoloc.autolocapi.io.RapportGeneratorFactory;

@Service
@Primary
public class BasicAgenceServiceImpl extends AgenceServiceImpl {
    public BasicAgenceServiceImpl(IAgenceRepository agenceRepository, IEmployeRepository employeRepository,
                                  IVehiculeService vehiculeService, RapportGeneratorFactory rapportFactory) {
        super(agenceRepository, employeRepository, vehiculeService, rapportFactory);
    }
}
